package com.br.api_agendamento.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor; // Adicionamos NoArgsConstructor para facilitar a serialização/deserialização

@Data
@AllArgsConstructor // Construtor com todos os argumentos
@NoArgsConstructor // Construtor vazio (boa prática para DTOs)
public class LoginResponseDTO {
    
    // O JWT gerado que será usado nas requisições subsequentes
    private String token;
    
    // O tipo de token, que é "Bearer" para o JWT
    private String type = "Bearer";
    
    // ID do usuário logado (opcional, mas útil para o frontend)
    private Long id;
    
    // O papel/permissão do usuário logado (CLIENTE, INSTRUTOR, ADMIN)
    private String role;
}