package com.br.api_agendamento.dto;

import com.br.api_agendamento.model.Instrutor;
import lombok.Data;

@Data
public class InstrutorResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String especialidade;

    public InstrutorResponseDTO(Instrutor instrutor) {
        this.id = instrutor.getId();

        if (instrutor.getUsuario() != null) {
            this.nome = instrutor.getUsuario().getNome();
            this.email = instrutor.getUsuario().getEmail();
            this.telefone = instrutor.getUsuario().getTelefone();
        }

        // Extrai os dados próprios do Instrutor
        this.especialidade = instrutor.getEspecialidade();
    }

    public InstrutorResponseDTO() {
    }
}