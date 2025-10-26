package com.br.api_agendamento.controller;

import com.br.api_agendamento.dto.UsuarioRequestDTO;
import com.br.api_agendamento.model.Usuario;
import com.br.api_agendamento.services.UsuarioService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // POST: Cria um novo usuário (Acesso liberado no SecurityConfig)
    @PostMapping
    public ResponseEntity<Usuario> criarUsuario(@RequestBody @Valid UsuarioRequestDTO dto) {
        Usuario novoUsuario = usuarioService.salvar(dto);
        return new ResponseEntity<>(novoUsuario, HttpStatus.CREATED);
    }

    // GET: Lista todos os usuários
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // APENAS ADMIN pode listar TODOS os usuários
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(usuarioService.buscarTodos());
    }

    // GET: Busca usuário por ID
    @GetMapping("/{id}")
    // 🔑 CORREÇÃO: Deve ser ADMIN ou o ID da requisição deve ser o ID do usuário logado
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id") 
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // PUT: Atualiza um usuário
    @PutMapping("/{id}")
    // 🔑 CORREÇÃO: Deve ser ADMIN ou o ID da requisição deve ser o ID do usuário logado
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id") 
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable Long id,
                                                    @RequestBody @Valid UsuarioRequestDTO dto) {
        Usuario usuarioAtualizado = usuarioService.atualizar(id, dto);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    // DELETE: Deleta um usuário
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // APENAS ADMIN pode deletar usuários
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        // CORREÇÃO do nome do método para o service (se o nome for deletarPorId)
        usuarioService.deletarPorId(id); 
        return ResponseEntity.noContent().build();
    }
}