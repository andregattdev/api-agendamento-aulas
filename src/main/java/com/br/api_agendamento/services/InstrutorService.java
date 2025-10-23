package com.br.api_agendamento.services;

import com.br.api_agendamento.dto.InstrutorRequestDTO; // Importe o DTO
import com.br.api_agendamento.model.Instrutor;
import com.br.api_agendamento.repositories.InstrutorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class InstrutorService {

    @Autowired
    private InstrutorRepository instrutorRepository;

    
    public Instrutor salvar(InstrutorRequestDTO dto) {
        // Lógica de Negócio: Verificar se E-mail já existe
        if (instrutorRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado para outro instrutor.");
        }
        
       
        Instrutor instrutor = new Instrutor();
        instrutor.setNome(dto.getNome());
        instrutor.setEmail(dto.getEmail());
        instrutor.setEspecialidade(dto.getEspecialidade());
        // Configure outros campos do Instrutor, se houver.
        
        return instrutorRepository.save(instrutor);
    }

    // UPDATE: Recebe o ID e o DTO, busca, atualiza e salva
    public Instrutor atualizar(Long id, InstrutorRequestDTO dto) {
        return buscarPorId(id).map(instrutorExistente -> {
            // Lógica de Negócio: Se o email mudou, verificar unicidade
            if (!instrutorExistente.getEmail().equals(dto.getEmail())) {
                 if (instrutorRepository.findByEmail(dto.getEmail()).isPresent()) {
                    throw new RuntimeException("Novo e-mail já cadastrado para outro instrutor.");
                }
            }
            
            // Atualiza os campos
            instrutorExistente.setNome(dto.getNome());
            instrutorExistente.setEmail(dto.getEmail());
            instrutorExistente.setEspecialidade(dto.getEspecialidade());
            
            return instrutorRepository.save(instrutorExistente);
        }).orElseThrow(() -> new RuntimeException("Instrutor não encontrado com ID: " + id));
    }
    
    
    
    public List<Instrutor> buscarTodos() {
        return instrutorRepository.findAll();
    }

    public Optional<Instrutor> buscarPorId(Long id) {
        return instrutorRepository.findById(id);
    }
    
    public void deletarPorId(Long id) {
        if (!instrutorRepository.existsById(id)) {
            throw new RuntimeException("Instrutor não encontrado com ID: " + id);
        }
        instrutorRepository.deleteById(id);
    }

    public List<Instrutor> buscarPorEspecialidade(String especialidade) {
        return instrutorRepository.findByEspecialidadeContainingIgnoreCase(especialidade);
    }
}