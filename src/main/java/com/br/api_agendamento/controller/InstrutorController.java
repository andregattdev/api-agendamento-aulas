package com.br.api_agendamento.controller;

import com.br.api_agendamento.dto.InstrutorRequestDTO;
import com.br.api_agendamento.model.Instrutor;
import com.br.api_agendamento.services.InstrutorService;

import jakarta.validation.Valid; // Importante para as validações do DTO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/instrutores")
public class InstrutorController {

    @Autowired
    private  InstrutorService instrutorService;

    // POST: Cria um novo instrutor
    @PostMapping
    // @Valid: Dispara as validações do Bean Validation definidas no InstrutorRequestDTO
    public ResponseEntity<Instrutor> criarInstrutor(@RequestBody @Valid InstrutorRequestDTO dto) {
        Instrutor novoInstrutor = instrutorService.salvar(dto);
        // Retorna HTTP 201 CREATED
        return new ResponseEntity<>(novoInstrutor, HttpStatus.CREATED); 
    }

    // GET: Lista todos os instrutores
    @GetMapping
    public ResponseEntity<List<Instrutor>> listarTodos() {
        List<Instrutor> instrutores = instrutorService.buscarTodos();
        return ResponseEntity.ok(instrutores);
    }

    // GET: Busca instrutor por ID
    @GetMapping("/{id}")
    public ResponseEntity<Instrutor> buscarPorId(@PathVariable Long id) {
        // Usa o orElse para retornar HTTP 404 se não for encontrado
        return instrutorService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build()); 
    }

    // PUT: Atualiza um instrutor
    @PutMapping("/{id}")
    public ResponseEntity<Instrutor> atualizarInstrutor(@PathVariable Long id, 
                                                        @RequestBody @Valid InstrutorRequestDTO dto) {
        // A lógica de exceção (Instrutor não encontrado) está no Service
        Instrutor instrutorAtualizado = instrutorService.atualizar(id, dto);
        return ResponseEntity.ok(instrutorAtualizado);
    }

    // DELETE: Deleta um instrutor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarInstrutor(@PathVariable Long id) {
        instrutorService.deletarPorId(id);
        // Retorna HTTP 204 NO CONTENT
        return ResponseEntity.noContent().build(); 
    }
}