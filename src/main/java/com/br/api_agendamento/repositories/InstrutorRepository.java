package com.br.api_agendamento.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.api_agendamento.model.Instrutor;
import com.br.api_agendamento.model.Servico;

public interface InstrutorRepository extends JpaRepository<Servico, Long> {
    List<Instrutor> findByEspecialidadeContainingIgnoreCase(String especialidade);

}
