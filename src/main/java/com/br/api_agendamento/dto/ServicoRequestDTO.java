package com.br.api_agendamento.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

// Usamos @Data do Lombok para gerar Getters, Setters, etc.
@lombok.Data
public class ServicoRequestDTO {

    @NotBlank(message = "O nome do serviço é obrigatório.")
    @jakarta.validation.constraints.Size(max = 100, message = "O nome não pode exceder 100 caracteres.")
    private String nome;

    // A duração do serviço em minutos
    @NotNull(message = "A duração em minutos é obrigatória.")
    @Min(value = 15, message = "A duração mínima do serviço deve ser de 15 minutos.")
    private Integer duracaoMinutos;

    // O preço do serviço. Deve ser não nulo e maior ou igual a zero.
    @NotNull(message = "O preço é obrigatório.")
    @DecimalMin(value = "0.00", inclusive = true, message = "O preço não pode ser negativo.")
    private BigDecimal preco;
    
    // O ID do instrutor principal para este serviço
    // (Apesar de o Servico ter ManyToOne com Instrutor,
    // na criação, o cliente envia apenas o ID)
    @NotNull(message = "O ID do instrutor não pode ser nulo.")
    private Long instrutorId; 
}