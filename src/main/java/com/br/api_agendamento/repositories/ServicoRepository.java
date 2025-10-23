package com.br.api_agendamento.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.api_agendamento.model.Instrutor;
import com.br.api_agendamento.model.Servico;

public interface ServicoRepository extends JpaRepository<Servico, Long> {

    // Método customizado para buscar todos os serviços oferecidos por um instrutor
    List<Servico> findByInstrutor(Instrutor instrutor);
    
    // Método customizado para buscar serviços por nome
    List<Servico> findByNomeContainingIgnoreCase(String nome);
}
