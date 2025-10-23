package com.br.api_agendamento.services;

import com.br.api_agendamento.dto.AgendamentoRequestDTO;
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

    public AgendamentoService(AgendamentoRepository agendamentoRepository,
            UsuarioRepository usuarioRepository,
            InstrutorRepository instrutorRepository,
            ServicoRepository servicoRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.instrutorRepository = instrutorRepository;
        this.servicoRepository = servicoRepository;
    }

    public Agendamento agendar(AgendamentoRequestDTO dto) {

        Usuario cliente = usuarioRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));

        Instrutor instrutor = instrutorRepository.findById(dto.getInstrutorId())
                .orElseThrow(() -> new RuntimeException("Instrutor não encontrado."));

        Servico servico = servicoRepository.findById(dto.getServicoId())
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado."));

        // 2. **REGRA DE NEGÓCIO: Data no Futuro**
        // A validação @FutureOrPresent já está no DTO, mas esta é a checagem final.
        if (dto.getDataHoraInicio().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Não é permitido agendar no passado.");
        }

        Agendamento novoAgendamento = new Agendamento();
        novoAgendamento.setCliente(cliente);
        novoAgendamento.setInstrutor(instrutor);
        novoAgendamento.setServico(servico);
        novoAgendamento.setDataHoraInicio(dto.getDataHoraInicio());
        novoAgendamento.setObservacoes(dto.getObservacoes());

        // Regra de Negócio Avançada 7: Calcular Hora de Fim
        LocalDateTime dataHoraFim = dto.getDataHoraInicio().plusMinutes(servico.getDuracaoMinutos());
        novoAgendamento.setDataHoraFim(dataHoraFim);

        return agendamentoRepository.save(novoAgendamento);
    }

    // DELETE (Com Regra de Negócio de Prazo)
    public void cancelar(Long id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado."));

        // Regra 4: Prazo de Cancelamento (Mínimo de 2 horas)
        long horasParaInicio = ChronoUnit.HOURS.between(LocalDateTime.now(), agendamento.getDataHoraInicio());

        if (horasParaInicio < 2) {
            throw new RuntimeException("Cancelamento negado. Antecedência mínima não respeitada.");
        }

        agendamentoRepository.delete(agendamento);
    }
}