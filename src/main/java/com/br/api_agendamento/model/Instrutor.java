package com.br.api_agendamento.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class Instrutor {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    private String nome;

    private String especialidade;

     @OneToMany(mappedBy = "instrutor", cascade = CascadeType.ALL)
     private List<Servico> servicos;

     // RELACIONAMENTO 1:1 com Usuario (Se o Instrutor tiver um login separado do Admin/Cliente):
     @OneToOne
     @JoinColumn(name = "usuario_id")
     private Usuario usuario;

}
