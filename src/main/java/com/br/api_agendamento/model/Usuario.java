package com.br.api_agendamento.model;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;



@Data
@Entity
public class Usuario {

    @Id
   
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    private String nome;

    private String email;

    private String telefone;


    private String senha; 

    @Enumerated(EnumType.STRING) 
    private TipoUsuario tipo = TipoUsuario.CLIENTE;


    public Usuario() {
    }

    
    public Usuario(Long id, String nome, String email, String telefone, String senha, TipoUsuario tipo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.senha = senha;
        this.tipo = tipo;
    }
}