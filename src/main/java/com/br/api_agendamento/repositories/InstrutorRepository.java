package com.br.api_agendamento.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.api_agendamento.model.Instrutor;


public interface InstrutorRepository extends JpaRepository<Instrutor, Long> {
    List<Instrutor> findByEspecialidadeContainingIgnoreCase(String especialidade);
    Optional<Instrutor> findByUsuarioEmail(String email);


}
