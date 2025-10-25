package com.br.api_agendamento.services;

import com.br.api_agendamento.dto.AgendamentoRequestDTO;
import com.br.api_agendamento.exception.RecursoNaoEncontradoException; // NOVO
import com.br.api_agendamento.exception.RegraDeNegocioException;      // NOVO
import com.br.api_agendamento.model.Agendamento;
import com.br.api_agendamento.model.Usuario;
import com.br.api_agendamento.repositories.AgendamentoRepository;
import com.br.api_agendamento.repositories.InstrutorRepository;
import com.br.api_agendamento.repositories.ServicoRepository;
import com.br.api_agendamento.repositories.UsuarioRepository;

import com.br.api_agendamento.model.Instrutor;
import com.br.api_agendamento.model.Servico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class AgendamentoService {

    @Autowired
    private  AgendamentoRepository agendamentoRepository;
    
    @Autowired
    private  UsuarioRepository usuarioRepository;

    @Autowired
    private  InstrutorRepository instrutorRepository;

    @Autowired
    private  ServicoRepository servicoRepository;

    // CREATE
    public Agendamento agendar(AgendamentoRequestDTO dto) {
        
        // ... (Validações de Cliente, Instrutor e Serviço - HTTP 404)
        Usuario cliente = usuarioRepository.findById(dto.getClienteId())
            .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado."));
        
        Instrutor instrutor = instrutorRepository.findById(dto.getInstrutorId())
            .orElseThrow(() -> new RecursoNaoEncontradoException("Instrutor não encontrado."));
            
        Servico servico = servicoRepository.findById(dto.getServicoId())
            .orElseThrow(() -> new RecursoNaoEncontradoException("Serviço não encontrado."));
        
        // 2. Regra de Negócio: Data no Futuro (Regra 400)
        if (dto.getDataHoraInicio().isBefore(LocalDateTime.now())) {
            throw new RegraDeNegocioException("Não é permitido agendar no passado.");
        }
        
        // CALCULA O FIM DO AGENDAMENTO (Necessário para a checagem de conflito)
        LocalDateTime novaDataHoraFim = dto.getDataHoraInicio().plusMinutes(servico.getDuracaoMinutos());
        
        // 3. REGRA DE NEGÓCIO ESSENCIAL: Checar Disponibilidade (Regra 400)
        // Usa o novo método do Repository
        if (agendamentoRepository.existeConflito(
            instrutor.getId(), 
            dto.getDataHoraInicio(), 
            novaDataHoraFim)) 
        {
            throw new RegraDeNegocioException("Instrutor já está ocupado neste horário. Conflito detectado.");
        }
        
        // Mapeamento DTO -> Entidade
        Agendamento novoAgendamento = new Agendamento();
        novoAgendamento.setCliente(cliente);
        novoAgendamento.setInstrutor(instrutor);
        novoAgendamento.setServico(servico);
        novoAgendamento.setDataHoraInicio(dto.getDataHoraInicio());
        novoAgendamento.setObservacoes(dto.getObservacoes());
        
        // Regra de Negócio Avançada 7: Seta a Hora de Fim calculada
        novoAgendamento.setDataHoraFim(novaDataHoraFim); 
        
        return agendamentoRepository.save(novoAgendamento);
    }

    // READ ALL
    public List<Agendamento> buscarTodos() {
        return agendamentoRepository.findAll();
    }

    // READ BY ID
    public Optional<Agendamento> buscarPorId(Long id) {
        return agendamentoRepository.findById(id);
    }

    // DELETE (Cancelamento)
    public void cancelar(Long id) {
        // 404
        Agendamento agendamento = agendamentoRepository.findById(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Agendamento não encontrado."));

        // Regra de Negócio Essencial 4: Prazo de Cancelamento (Regra 400)
        long horasParaInicio = ChronoUnit.HOURS.between(LocalDateTime.now(), agendamento.getDataHoraInicio());
        
        // O valor 2 (2 horas) é um exemplo.
        if (horasParaInicio < 2) {
            throw new RegraDeNegocioException("Cancelamento negado. O agendamento deve ser cancelado com no mínimo 2 horas de antecedência.");
        }

        agendamentoRepository.delete(agendamento);
    }
}