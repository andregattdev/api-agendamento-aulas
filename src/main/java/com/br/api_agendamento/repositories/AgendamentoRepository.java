package com.br.api_agendamento.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.br.api_agendamento.model.Agendamento;
import com.br.api_agendamento.model.StatusAgendamento;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    // Checa se há conflito de horário para um instrutor
    @Query("SELECT COUNT(a) > 0 FROM Agendamento a " +
            "WHERE a.instrutor.id = :instrutorId " +
            "AND a.status IN :statusList " +
            "AND (" +
            "a.dataHoraInicio < :novaDataHoraFim AND :novaDataHoraInicio < a.dataHoraFim" +
            ")")
    boolean existeConflito(
            @Param("instrutorId") Long instrutorId,
            @Param("novaDataHoraInicio") LocalDateTime novaDataHoraInicio,
            @Param("novaDataHoraFim") LocalDateTime novaDataHoraFim,
            @Param("statusList") List<StatusAgendamento> statusList);

    List<Agendamento> findByInstrutorIdAndDataHoraInicioBetweenAndStatus(
            Long instrutorId,
            LocalDateTime inicio,
            LocalDateTime fim,
            StatusAgendamento status);

    List<Agendamento> findByClienteId(Long clienteId);

    List<Agendamento> findByInstrutorId(Long instrutorId);
}