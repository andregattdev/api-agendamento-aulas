package com.br.api_agendamento.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.api_agendamento.model.Instrutor;


public interface InstrutorRepository extends JpaRepository<Instrutor, Long> {
    List<Instrutor> findByEspecialidadeContainingIgnoreCase(String especialidade);

}
