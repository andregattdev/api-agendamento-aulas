package com.br.api_agendamento.services;

import com.br.api_agendamento.dto.ServicoRequestDTO;
import com.br.api_agendamento.model.Servico;
import com.br.api_agendamento.repositories.InstrutorRepository;
import com.br.api_agendamento.repositories.ServicoRepository;
import com.br.api_agendamento.model.Instrutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private InstrutorRepository instrutorRepository;

    // CREATE: Recebe o DTO
    public Servico salvar(ServicoRequestDTO dto) {

        // Regra de Entidade: O Instrutor deve existir
        Instrutor instrutor = instrutorRepository.findById(dto.getInstrutorId())
                .orElseThrow(() -> new RuntimeException("Instrutor não encontrado com ID: " + dto.getInstrutorId()));

        // Mapeamento DTO -> Entidade
        Servico servico = new Servico();
        servico.setNome(dto.getNome());
        servico.setDuracaoMinutos(dto.getDuracaoMinutos());
        servico.setPreco(dto.getPreco());
        servico.setInstrutor(instrutor); // Seta a Entidade Instrutor

        return servicoRepository.save(servico);
    }


    public Servico atualizar(Long id, ServicoRequestDTO dto) {

        
        Instrutor instrutor = instrutorRepository.findById(dto.getInstrutorId())
                .orElseThrow(() -> new RuntimeException("Instrutor não encontrado com ID: " + dto.getInstrutorId()));

       
        return buscarPorId(id).map(servicoExistente -> {
            servicoExistente.setNome(dto.getNome());
            servicoExistente.setDuracaoMinutos(dto.getDuracaoMinutos());
            servicoExistente.setPreco(dto.getPreco());
            servicoExistente.setInstrutor(instrutor); 

            return servicoRepository.save(servicoExistente);
        }).orElseThrow(() -> new RuntimeException("Serviço não encontrado com ID: " + id));
    }

   
    public List<Servico> buscarTodos() {
        return servicoRepository.findAll();
    }
    
    // NOVO MÉTODO: READ BY ID (Retorna um Optional<Servico>)
    public Optional<Servico> buscarPorId(Long id) {
        return servicoRepository.findById(id);
    }
    
    // NOVO MÉTODO: DELETE
    public void deletarPorId(Long id) {
        
        if (!servicoRepository.existsById(id)) {
            
            throw new RuntimeException("Serviço não encontrado com ID: " + id);
        }
        
      
        
        servicoRepository.deleteById(id);
    }
}