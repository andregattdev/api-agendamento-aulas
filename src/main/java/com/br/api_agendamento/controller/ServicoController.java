package com.br.api_agendamento.controller;

import com.br.api_agendamento.dto.ServicoRequestDTO;
import com.br.api_agendamento.model.Servico;
import com.br.api_agendamento.services.ServicoService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    @Autowired
    private ServicoService servicoService;

    // POST: Cria um novo serviço/aula
    @PostMapping
    public ResponseEntity<Servico> criarServico(@RequestBody @Valid ServicoRequestDTO dto) {
        Servico novoServico = servicoService.salvar(dto);
        return new ResponseEntity<>(novoServico, HttpStatus.CREATED);
    }

    // GET: Lista todos os serviços/aulas
    @GetMapping
    public ResponseEntity<List<Servico>> listarTodos() {
        return ResponseEntity.ok(servicoService.buscarTodos());
    }

    // GET: Busca serviço por ID
    @GetMapping("/{id}")
    public ResponseEntity<Servico> buscarPorId(@PathVariable Long id) {
        return servicoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // PUT: Atualiza um serviço
    @PutMapping("/{id}")
    public ResponseEntity<Servico> atualizarServico(@PathVariable Long id, 
                                                      @RequestBody @Valid ServicoRequestDTO dto) {
        Servico servicoAtualizado = servicoService.atualizar(id, dto);
        return ResponseEntity.ok(servicoAtualizado);
    }

    // DELETE: Deleta um serviço
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarServico(@PathVariable Long id) {
        servicoService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }
}