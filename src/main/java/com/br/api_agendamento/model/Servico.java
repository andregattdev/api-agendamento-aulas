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

@Data
@Entity
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome; 
    
    // CORRIGIDO: Mapeamento mais simples para o Banco de Dados
    // Armazena a duração em minutos (ex: 60, 90, 30)
    private Integer duracaoMinutos; 
    
    
    private BigDecimal preco; 

    @ManyToOne 
    @JoinColumn(name = "instrutor_id", nullable = false)
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