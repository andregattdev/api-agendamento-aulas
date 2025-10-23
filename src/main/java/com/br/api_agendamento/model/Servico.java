package com.br.api_agendamento.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import jakarta.persistence.JoinColumn;
import java.math.BigDecimal; // Para o preço
import java.time.Duration; // Para a duração

@Data
@Entity
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome; // Ex: "Corte de Cabelo", "Aula de Guitarra Nível 1"
    
    // A duração da aula/serviço (em minutos, horas, etc.)
    private Duration duracao; 

    
    private BigDecimal preco; 

    @ManyToOne 
    @JoinColumn(name = "instrutor_id", nullable = false)
    private Instrutor instrutor;

    

    public Servico() {}
    
    public Servico(String nome, Duration duracao, BigDecimal preco, Instrutor instrutor) {
        this.nome = nome;
        this.duracao = duracao;
        this.preco = preco;
        this.instrutor = instrutor;
    }


}