package com.br.api_agendamento.repositories;

import com.br.api_agendamento.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    // NOVO MÉTODO: Checa se há conflito de horário para um instrutor
    @Query("SELECT COUNT(a) > 0 FROM Agendamento a " +
           "WHERE a.instrutor.id = :instrutorId " + // 1. Mesmo instrutor
           "AND (" +
           // 2. Condição de sobreposição:
           // O agendamento existente (a) começa antes do novo terminar (novaDataHoraFim)
           // E o novo agendamento (novaDataHoraInicio) começa antes do existente terminar (a.dataHoraFim)
           "    a.dataHoraInicio < :novaDataHoraFim AND :novaDataHoraInicio < a.dataHoraFim" +
           ")")
    boolean existeConflito(
            @Param("instrutorId") Long instrutorId,
            @Param("novaDataHoraInicio") LocalDateTime novaDataHoraInicio,
            @Param("novaDataHoraFim") LocalDateTime novaDataHoraFim);
    
    // Outros métodos READ...
    List<Agendamento> findByClienteId(Long clienteId);
}