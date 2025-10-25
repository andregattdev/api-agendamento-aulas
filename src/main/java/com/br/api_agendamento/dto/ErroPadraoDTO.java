package com.br.api_agendamento.dto;

import java.time.LocalDateTime;
import java.util.List;

// Objeto que representa a resposta de erro padronizada
@lombok.Data
public class ErroPadraoDTO {
    
    private LocalDateTime timestamp;
    private Integer status; // Status HTTP
    private String error;   // Nome do Status HTTP (ex: "Bad Request")
    private String message; // Mensagem principal (geralmente a mensagem da exceção)
    private List<String> details; // Lista de erros de validação (para @Valid)
    private String path;    // O endpoint que gerou o erro

    public ErroPadraoDTO(Integer status, String error, String message, String path, List<String> details) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.details = details;
    }
}