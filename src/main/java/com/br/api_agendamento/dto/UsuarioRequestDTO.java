package com.br.api_agendamento.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioRequestDTO {

    @NotBlank(message = "O nome do usuário é obrigatório.")
    @Size(max = 100, message = "O nome não pode exceder 100 caracteres.")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Formato de e-mail inválido.")
    @Size(max = 100, message = "O e-mail não pode exceder 100 caracteres.")
    private String email;

    @NotBlank(message = "O telefone é obrigatório.")
    @Size(min = 8, max = 15, message = "O telefone deve ter entre 8 e 15 dígitos.")
    private String telefone;

   
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
     private String senha; 

    
}