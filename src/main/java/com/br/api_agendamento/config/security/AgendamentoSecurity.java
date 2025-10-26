package com.br.api_agendamento.config.security;

import com.br.api_agendamento.model.Agendamento;
import com.br.api_agendamento.services.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// Importante: O nome do Bean deve ser 'agendamentoSecurity' para 
// que o @PreAuthorize no Controller possa referenciá-lo.
@Component("agendamentoSecurity")
public class AgendamentoSecurity {

    // Injetamos o Service para buscar os dados do Agendamento
    @Autowired
    private AgendamentoService agendamentoService;

    /**
     * Verifica se o ID do usuário logado (principalId) é o cliente proprietário do
     * agendamento.
     * Usado para GET, PATCH /cancelar.
     * * @param agendamentoId O ID do agendamento a ser verificado.
     * 
     * @param principalId O ID do usuário logado (extraído do token).
     * @return true se o usuário logado for o cliente proprietário.
     */
    public boolean isOwner(Long agendamentoId, Long principalId) {
        try {
            Agendamento agendamento = agendamentoService.buscarPorId(agendamentoId);

            // Compara o ID do cliente do agendamento com o ID do usuário logado
            return agendamento.getCliente().getId().equals(principalId);
        } catch (Exception e) {
            // Se o agendamento não for encontrado, nega o acesso
            return false;
        }
    }

    /**
     * Verifica se o ID do usuário logado (principalId) é o instrutor envolvido no
     * agendamento.
     * Usado para GET, PATCH /confirmar, PATCH /cancelar.
     * * @param agendamentoId O ID do agendamento a ser verificado.
     * 
     * @param principalId O ID do usuário logado (extraído do token).
     * @return true se o usuário logado for o instrutor envolvido.
     */
    public boolean isInstructorInvolved(Long agendamentoId, Long principalId) {
        try {
            Agendamento agendamento = agendamentoService.buscarPorId(agendamentoId);

            // Nota: O Instrutor tem uma referência ao Usuario (ID do Instrutor/Usuário)
            // Compara o ID do USUARIO que está ligado ao INSTRUTOR com o ID do usuário
            // logado
            return agendamento.getInstrutor().getUsuario().getId().equals(principalId);
        } catch (Exception e) {
            // Se o agendamento não for encontrado, nega o acesso
            return false;
        }
    }
}