package com.br.api_agendamento.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class AgendamentoRequestDTO {
    
    // O ID do usuário que está fazendo o agendamento
    @NotNull(message = "O ID do cliente não pode ser nulo.")
    private Long clienteId;
    
    // O ID do instrutor desejado
    @NotNull(message = "O ID do instrutor não pode ser nulo.")
    private Long instrutorId;
    
    // O ID do serviço a ser agendado (ex: aula de natação)
    @NotNull(message = "O ID do serviço não pode ser nulo.")
    private Long servicoId;
    
    // Data e hora de início. Deve ser no presente ou futuro.
    @NotNull(message = "A data e hora de início não pode ser nula.")
    @FutureOrPresent(message = "O agendamento deve ser para uma data e hora futura.")
    private LocalDateTime dataHoraInicio;
    
    // O DTO pode ter outros campos, como 'observacoes' se necessário.
    private String observacoes;
}
