package com.br.api_agendamento.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.Data;

import java.time.LocalDateTime; // Ideal para data e hora precisas

// Enum para o Status do Agendamento (vamos criar este abaixo)


@Data
@Entity
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataHoraInicio;

    
    private LocalDateTime dataHoraFim; 

    @ManyToOne 
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente; 

    // 2. O Serviço/Aula (Um Agendamento tem 1 Serviço)
    @ManyToOne 
    @JoinColumn(name = "servico_id", nullable = false)
    private Servico servico;

    @ManyToOne 
    @JoinColumn(name = "instrutor_id", nullable = false)
    private Instrutor instrutor;

    @Enumerated(EnumType.STRING)
    private StatusAgendamento status;
    
    private String observacoes; 

   
}