package com.br.api_agendamento.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento Many-to-One: O agendamento é feito por um cliente (Usuario)
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente;

    // Relacionamento Many-to-One: O serviço agendado
    @ManyToOne
    @JoinColumn(name = "servico_id", nullable = false)
    private Servico servico;

    // Relacionamento Many-to-One: O instrutor que prestará o serviço.
    @ManyToOne
    @JoinColumn(name = "instrutor_id", nullable = false)
    private Instrutor instrutor;

    // Data e Hora do início do agendamento
    @Column(nullable = false)
    private LocalDateTime dataHoraInicio;

 
    @Column(nullable = false)
    private LocalDateTime dataHoraFim;

    // Status do agendamento (Ex: PENDENTE, CONFIRMADO, CANCELADO, CONCLUIDO)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAgendamento status;
}