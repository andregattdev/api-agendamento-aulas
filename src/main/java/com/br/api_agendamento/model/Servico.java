package com.br.api_agendamento.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import jakarta.persistence.JoinColumn;
import java.math.BigDecimal; 
// Remova: import java.time.Duration; 

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome; 
    
    private Integer duracaoMinutos; 
    
    
    private BigDecimal preco; 

    @ManyToOne 
    @JoinColumn(name = "instrutor_id", nullable = false)
    @JsonIgnore
    private Instrutor instrutor;


    public Servico() {}
    
    // Altere o construtor para aceitar Integer
    public Servico(String nome, Integer duracaoMinutos, BigDecimal preco, Instrutor instrutor) {
        this.nome = nome;
        this.duracaoMinutos = duracaoMinutos; 
        this.preco = preco;
        this.instrutor = instrutor;
    }
}