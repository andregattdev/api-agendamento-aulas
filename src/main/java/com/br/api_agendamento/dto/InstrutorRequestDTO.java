package com.br.api_agendamento.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class InstrutorRequestDTO {

    @NotBlank(message = "O nome é obrigatório.")
    @Size(max = 100, message = "O nome não pode exceder 100 caracteres.")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Formato de e-mail inválido.")
    private String email;

    @NotBlank(message = "O telefone é obrigatório.") 
    private String telefone; 

    @NotBlank(message = "A senha é obrigatória.") 
    private String senha; 
    
    @NotBlank(message = "A especialidade é obrigatória.")
    private String especialidade;
}