package com.br.api_agendamento.config.security;

import com.br.api_agendamento.model.Agendamento;
import com.br.api_agendamento.services.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// Este Bean é injetado e usado no @PreAuthorize do Controller
@Component("agendamentoSecurity")
public class AgendamentoSecurity {

    @Autowired
    private AgendamentoService agendamentoService;

    // Verifica se o ID do usuário logado (principalId) é o cliente do agendamento
    public boolean isOwner(Long agendamentoId, Long principalId) {
        try {
            Agendamento agendamento = agendamentoService.buscarPorId(agendamentoId);
            return agendamento.getCliente().getId().equals(principalId);
        } catch (Exception e) {
            // Se o agendamento não for encontrado, nega o acesso
            return false; 
        }
    }

    // Verifica se o ID do usuário logado (principalId) é o instrutor do agendamento
    public boolean isInstructorInvolved(Long agendamentoId, Long principalId) {
        try {
            Agendamento agendamento = agendamentoService.buscarPorId(agendamentoId);
            return agendamento.getInstrutor().getUsuario().getId().equals(principalId); // Compara com o ID do USUARIO do Instrutor
        } catch (Exception e) {
            // Se o agendamento não for encontrado, nega o acesso
            return false; 
        }
    }
}