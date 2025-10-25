package com.br.api_agendamento.controller;

import com.br.api_agendamento.dto.LoginRequestDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequestDTO dto) {
        
        // 1. Cria o token com as credenciais do DTO
        UsernamePasswordAuthenticationToken authToken = 
            new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha());
        
        // 2. Tenta autenticar
        // O AuthenticationManager chama nosso AutenticacaoService e usa o PasswordEncoder.
        try {
            Authentication authentication = authenticationManager.authenticate(authToken);
            
            // Se a autenticação for bem-sucedida, você retornaria um JWT aqui.
            // Por enquanto, retornamos um status de sucesso e o email (Username).
            return ResponseEntity.ok("Login bem-sucedido para o usuário: " + authentication.getName());
            
        } catch (Exception e) {
            // Se falhar (senha incorreta ou usuário não encontrado)
            return ResponseEntity.status(401).body("Credenciais inválidas: " + e.getMessage());
        }
    }
}