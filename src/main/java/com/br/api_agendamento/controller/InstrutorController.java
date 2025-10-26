package com.br.api_agendamento.controller;

import com.br.api_agendamento.dto.InstrutorRequestDTO;
import com.br.api_agendamento.dto.InstrutorResponseDTO; // NOVO IMPORT
import com.br.api_agendamento.model.Instrutor;
import com.br.api_agendamento.services.InstrutorService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors; // Necessário para a conversão de lista

@RestController
@RequestMapping("/instrutores")
public class InstrutorController {

    @Autowired
    private InstrutorService instrutorService;

    // POST: Cria um novo instrutor
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    // Retorna InstrutorResponseDTO
    public ResponseEntity<InstrutorResponseDTO> criarInstrutor(@RequestBody @Valid InstrutorRequestDTO dto) {
        Instrutor novoInstrutor = instrutorService.salvar(dto);
        // Retorna o objeto mapeado para o DTO
        return new ResponseEntity<>(new InstrutorResponseDTO(novoInstrutor), HttpStatus.CREATED);
    }

    // GET: Lista todos os instrutores
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    // Retorna Lista de InstrutorResponseDTO
    public ResponseEntity<List<InstrutorResponseDTO>> listarTodos() {
        List<InstrutorResponseDTO> dtos = instrutorService.buscarTodos().stream()
                // Converte cada Instrutor para InstrutorResponseDTO usando o construtor
                .map(InstrutorResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // GET: Busca instrutor por ID
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    // Retorna InstrutorResponseDTO
    public ResponseEntity<InstrutorResponseDTO> buscarPorId(@PathVariable Long id) {
        // Mapeia o Optional<Instrutor> para Optional<InstrutorResponseDTO> antes de
        // construir a resposta
        return instrutorService.buscarPorId(id)
                .map(InstrutorResponseDTO::new)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // PUT: Atualiza um instrutor
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('INSTRUTOR') and #id == authentication.principal.id)")
    // Retorna InstrutorResponseDTO
    public ResponseEntity<InstrutorResponseDTO> atualizarInstrutor(@PathVariable Long id,
            @RequestBody @Valid InstrutorRequestDTO dto) {
        Instrutor instrutorAtualizado = instrutorService.atualizar(id, dto);
        // Retorna o objeto mapeado para o DTO
        return ResponseEntity.ok(new InstrutorResponseDTO(instrutorAtualizado));
    }

    // DELETE: Deleta um instrutor
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletarInstrutor(@PathVariable Long id) {
        instrutorService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }
}