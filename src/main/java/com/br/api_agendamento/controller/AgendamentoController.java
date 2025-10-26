package com.br.api_agendamento.controller;

import com.br.api_agendamento.dto.AgendamentoRequestDTO;
import com.br.api_agendamento.model.Agendamento;
import com.br.api_agendamento.model.Usuario;
import com.br.api_agendamento.services.AgendamentoService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService agendamentoService;

  
    // 1. CREATE: Criar Agendamento (POST)

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')") 
    public ResponseEntity<Agendamento> criarAgendamento(
            @RequestBody @Valid AgendamentoRequestDTO dto,
            Authentication authentication
    ) {
        
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        Long clienteId = usuarioLogado.getId(); 

        Agendamento novoAgendamento = agendamentoService.agendar(clienteId, dto);

        return new ResponseEntity<>(novoAgendamento, HttpStatus.CREATED);
    }


    // Listar TODOS os agendamentos
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // Apenas ADMIN pode listar todos
    public ResponseEntity<List<Agendamento>> listarTodos() {
        List<Agendamento> agendamentos = agendamentoService.buscarTodos();
        return ResponseEntity.ok(agendamentos);
    }

    // Buscar Agendamento por ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or " + 
                  // Cliente proprietário
                  "(hasRole('CLIENTE') and @agendamentoSecurity.isOwner(#id, authentication.principal)) or " +
                  // Instrutor envolvido
                  "(hasRole('INSTRUTOR') and @agendamentoSecurity.isInstructorInvolved(#id, authentication.principal))")
    public ResponseEntity<Agendamento> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(agendamentoService.buscarPorId(id));
    }

    // Listar Agendamentos por Cliente (URL /cliente/ID)
    // ADMIN pode ver qualquer ID. Cliente só pode ver o próprio (#clienteId == authentication.principal)
    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("hasRole('ADMIN') or (#clienteId == authentication.principal)") 
    public ResponseEntity<List<Agendamento>> listarPorCliente(@PathVariable Long clienteId) {
        List<Agendamento> agendamentos = agendamentoService.buscarPorCliente(clienteId);
        return ResponseEntity.ok(agendamentos);
    }

    // Listar Agendamentos por Instrutor (URL /instrutor/ID)
    // ADMIN pode ver qualquer ID. Instrutor só pode ver o próprio
    @GetMapping("/instrutor/{instrutorId}")
    @PreAuthorize("hasRole('ADMIN') or (#instrutorId == authentication.principal)") 
    public ResponseEntity<List<Agendamento>> listarPorInstrutor(@PathVariable Long instrutorId) {
        List<Agendamento> agendamentos = agendamentoService.buscarPorInstrutor(instrutorId);
        return ResponseEntity.ok(agendamentos);
    }
    // 3. UPDATE: Atualização de Status (PATCH)
 

    // Confirmar Agendamento
    @PatchMapping("/{id}/confirmar")
    @PreAuthorize("hasRole('ADMIN') or " +
                  // Apenas o INSTRUTOR envolvido pode confirmar
                  "(hasRole('INSTRUTOR') and @agendamentoSecurity.isInstructorInvolved(#id, authentication.principal))")
    public ResponseEntity<Agendamento> confirmarAgendamento(@PathVariable Long id) {
        Agendamento agendamentoAtualizado = agendamentoService.confirmarAgendamento(id);
        return ResponseEntity.ok(agendamentoAtualizado);
    }

    // Cancelar Agendamento
    @PatchMapping("/{id}/cancelar")
    @PreAuthorize("hasRole('ADMIN') or " +
                  // Cliente proprietário ou o instrutor envolvido podem cancelar
                  "(hasRole('CLIENTE') and @agendamentoSecurity.isOwner(#id, authentication.principal)) or " +
                  "(hasRole('INSTRUTOR') and @agendamentoSecurity.isInstructorInvolved(#id, authentication.principal))")
    public ResponseEntity<Agendamento> cancelarAgendamento(@PathVariable Long id) {
        Agendamento agendamentoAtualizado = agendamentoService.cancelarAgendamento(id);
        return ResponseEntity.ok(agendamentoAtualizado);
    }
}