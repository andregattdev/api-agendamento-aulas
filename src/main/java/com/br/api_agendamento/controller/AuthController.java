package com.br.api_agendamento.controller;

import com.br.api_agendamento.dto.LoginRequestDTO;
import com.br.api_agendamento.dto.LoginResponseDTO;
import com.br.api_agendamento.model.Usuario;
import com.br.api_agendamento.services.jwt.TokenService;

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

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto) {
        
        UsernamePasswordAuthenticationToken authToken = 
            new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha());
        
        Authentication authentication = authenticationManager.authenticate(authToken);
        
        // 1. Converte o objeto autenticado (UserDetails/Usuario)
        Usuario usuario = (Usuario) authentication.getPrincipal(); 
        
        // 2. GERA o token JWT
        String jwtToken = tokenService.generateToken(usuario);
        
        // 3. Retorna a resposta com o token
        LoginResponseDTO response = new LoginResponseDTO(
            jwtToken, 
            "Bearer", 
            usuario.getId(), 
            usuario.getTipo().name()
        );
        
        return ResponseEntity.ok(response);
    }
}