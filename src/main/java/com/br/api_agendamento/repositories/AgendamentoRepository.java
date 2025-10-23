package com.br.api_agendamento.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.br.api_agendamento.model.Agendamento;
import com.br.api_agendamento.model.Instrutor;
import com.br.api_agendamento.model.Usuario;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    // ----------------------------------------------------------------------
    // MÉTODO CRUCIAL PARA A VALIDAÇÃO DE CONFLITO DE HORÁRIO
    // ----------------------------------------------------------------------
    
    /**
     * Busca agendamentos existentes para um instrutor que se sobrepõem ao período fornecido.
     * * Conflito ocorre se:
     * 1. O novo período de agendamento (start/end) começar ou terminar DENTRO de um agendamento existente, OU
     * 2. Um agendamento existente começar DENTRO do novo período.
     */
    @Query("SELECT a FROM Agendamento a " +
           "WHERE a.instrutor = :instrutor " +
           "AND a.status = 'CONFIRMADO' " + // Importante: Checar apenas agendamentos confirmados
           "AND ( " +
           // Caso 1: Novo agendamento começa antes e termina depois de um agendamento existente
           "    (:novoInicio < a.dataHoraFim AND :novoFim > a.dataHoraInicio) " +
           "    OR " +
           // Caso 2: Novo agendamento engloba totalmente um agendamento existente
           "    (:novoInicio <= a.dataHoraInicio AND :novoFim >= a.dataHoraFim) " +
           ")")
    List<Agendamento> findConflitos(
            @Param("instrutor") Instrutor instrutor,
            @Param("novoInicio") LocalDateTime novoInicio,
            @Param("novoFim") LocalDateTime novoFim
    );
    
    // Método customizado para listar agendamentos de um cliente (Usuário)
    List<Agendamento> findByCliente(Usuario cliente);
    
    // Método customizado para listar todos os agendamentos de um instrutor
    List<Agendamento> findByInstrutor(Instrutor instrutor);
}
