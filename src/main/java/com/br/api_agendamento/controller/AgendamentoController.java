package com.br.api_agendamento.controller;

import com.br.api_agendamento.dto.AgendamentoRequestDTO;
import com.br.api_agendamento.model.Agendamento;
import com.br.api_agendamento.services.AgendamentoService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService agendamentoService;

    // POST: Cria um novo agendamento (Regras de Disponibilidade e Futuro)
    @PostMapping
    public ResponseEntity<Agendamento> agendar(@RequestBody @Valid AgendamentoRequestDTO dto) {
        // Todas as regras de negócio (disponibilidade, entidade, data futura) são checadas aqui
        Agendamento novoAgendamento = agendamentoService.agendar(dto); 
        return new ResponseEntity<>(novoAgendamento, HttpStatus.CREATED);
    }

    // GET: Lista todos os agendamentos (Para ADMIN)
    @GetMapping
    public ResponseEntity<List<Agendamento>> listarTodos() {
        return ResponseEntity.ok(agendamentoService.buscarTodos());
    }

    // GET: Busca agendamento por ID
    @GetMapping("/{id}")
    public ResponseEntity<Agendamento> buscarPorId(@PathVariable Long id) {
        return agendamentoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE: Cancela um agendamento (Regra de Prazo Mínimo)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        // A lógica de negócio do prazo mínimo de cancelamento está no Service
        agendamentoService.cancelar(id);
        return ResponseEntity.noContent().build();
    }

    // **NOTA:** Implementação do PUT (Atualizar) e filtros de busca avançada
    // (Regra Avançada 6 e 8) podem ser feitos em um segundo momento.
}