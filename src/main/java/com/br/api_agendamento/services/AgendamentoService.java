package com.br.api_agendamento.services;

import com.br.api_agendamento.dto.AgendamentoRequestDTO;
import com.br.api_agendamento.exception.RecursoNaoEncontradoException;
import com.br.api_agendamento.exception.RegraDeNegocioException;
import com.br.api_agendamento.model.*;
import com.br.api_agendamento.repositories.AgendamentoRepository;
import com.br.api_agendamento.repositories.InstrutorRepository;
import com.br.api_agendamento.repositories.ServicoRepository;
import com.br.api_agendamento.repositories.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

import static com.br.api_agendamento.model.StatusAgendamento.*; // Importa PENDENTE, CONFIRMADO, etc.

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private InstrutorRepository instrutorRepository;
    @Autowired
    private ServicoRepository servicoRepository;

    private static final List<StatusAgendamento> STATUS_BLOQUEANTES = List.of(CONFIRMADO, PENDENTE);

    // Método de agendar (mantido)
    @Transactional
    public Agendamento agendar(Long clienteId, AgendamentoRequestDTO dto) {
        // ... (lógica de agendamento: validação de existência, duração, conflito, etc.)
        // [CÓDIGO ANTERIOR AQUI]
        
        Usuario cliente = usuarioRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado com ID: " + clienteId));
        
        Instrutor instrutor = instrutorRepository.findById(dto.getInstrutorId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Instrutor não encontrado com ID: " + dto.getInstrutorId()));

        Servico servico = servicoRepository.findById(dto.getServicoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Serviço não encontrado com ID: " + dto.getServicoId()));
        
        LocalDateTime inicio = dto.getDataHoraInicio();
        
        if (servico.getDuracaoMinutos() == null || servico.getDuracaoMinutos() <= 0) {
            throw new RegraDeNegocioException("Serviço sem duração definida.");
        }
        
        LocalDateTime fim = inicio.plusMinutes(servico.getDuracaoMinutos());

        if (inicio.getDayOfWeek() == DayOfWeek.SATURDAY || inicio.getDayOfWeek() == DayOfWeek.SUNDAY) {
             throw new RegraDeNegocioException("Agendamentos são permitidos apenas de segunda a sexta-feira.");
        }
        
        boolean haConflito = agendamentoRepository.existeConflito(
            dto.getInstrutorId(),
            inicio,
            fim,
            STATUS_BLOQUEANTES
        );

        if (haConflito) {
            throw new RegraDeNegocioException("O instrutor já possui um agendamento no período solicitado. Escolha outro horário.");
        }
        
        Agendamento novoAgendamento = new Agendamento();
        novoAgendamento.setCliente(cliente);
        novoAgendamento.setInstrutor(instrutor);
        novoAgendamento.setServico(servico);
        novoAgendamento.setDataHoraInicio(inicio);
        novoAgendamento.setDataHoraFim(fim);
        novoAgendamento.setStatus(PENDENTE);

        return agendamentoRepository.save(novoAgendamento);
    }
    
    // Método auxiliar para buscar por ID
    public Agendamento buscarPorId(Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Agendamento não encontrado com ID: " + id));
    }

    // Método para CONFIRMAR Agendamento
    @Transactional
    public Agendamento confirmarAgendamento(Long agendamentoId) {
        Agendamento agendamento = buscarPorId(agendamentoId);

        if (agendamento.getStatus() != PENDENTE) {
            throw new RegraDeNegocioException("Somente agendamentos PENDENTES podem ser confirmados. Status atual: " + agendamento.getStatus());
        }

        // Antes de confirmar, RE-VALIDAR o conflito, caso outro agendamento pendente tenha sido confirmado
        // Este é um ponto de atenção em sistemas concorrentes, mas vital para a integridade.
        int duracaoMinutos = agendamento.getServico().getDuracaoMinutos();
        LocalDateTime inicio = agendamento.getDataHoraInicio();
        LocalDateTime fim = inicio.plusMinutes(duracaoMinutos);
        
        // Verifica se há conflito COM OUTROS agendamentos confirmados
        List<Agendamento> conflitos = agendamentoRepository.findByInstrutorIdAndDataHoraInicioBetweenAndStatus(
                agendamento.getInstrutor().getId(),
                inicio,
                fim.minusSeconds(1), // Ajuste para BETWEEN
                CONFIRMADO
        );
        
        if (!conflitos.isEmpty()) {
            throw new RegraDeNegocioException("Não foi possível confirmar. O instrutor já possui um agendamento CONFIRMADO no período.");
        }
        
        agendamento.setStatus(CONFIRMADO);
        return agendamentoRepository.save(agendamento);
    }

    // Método para CANCELAR Agendamento
    @Transactional
    public Agendamento cancelarAgendamento(Long agendamentoId) {
        Agendamento agendamento = buscarPorId(agendamentoId);

        if (agendamento.getStatus() == CANCELADO || agendamento.getStatus() == CONCLUIDO || agendamento.getStatus() == REJEITADO) {
            throw new RegraDeNegocioException("Agendamento não pode ser cancelado pois já está em status final: " + agendamento.getStatus());
        }

        // Regra de Negócio: O cancelamento só pode ocorrer se a data de início ainda não passou
        if (agendamento.getDataHoraInicio().isBefore(LocalDateTime.now())) {
            throw new RegraDeNegocioException("Não é possível cancelar um agendamento cuja hora de início já passou.");
        }
        
        agendamento.setStatus(CANCELADO);
        return agendamentoRepository.save(agendamento);
    }
    
    // Outros métodos de leitura (mantidos)
    public List<Agendamento> buscarTodos() {
        return agendamentoRepository.findAll();
    }
    
    public List<Agendamento> buscarPorCliente(Long clienteId) {
        return agendamentoRepository.findByClienteId(clienteId);
    }

    public List<Agendamento> buscarPorInstrutor(Long instrutorId) {
        return agendamentoRepository.findByInstrutorId(instrutorId);
    }
}