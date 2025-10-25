package com.br.api_agendamento.services;

import com.br.api_agendamento.dto.InstrutorRequestDTO;
import com.br.api_agendamento.exception.RecursoNaoEncontradoException; // NOVO
import com.br.api_agendamento.exception.RegraDeNegocioException;      // NOVO
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

    

    // CREATE
    public Instrutor salvar(InstrutorRequestDTO dto) {
        // Lógica de Negócio: Verificar se E-mail já existe
        if (instrutorRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RegraDeNegocioException("E-mail já cadastrado para outro instrutor.");
        }
        
        Instrutor instrutor = new Instrutor();
        instrutor.setNome(dto.getNome());
        instrutor.setEmail(dto.getEmail());
        instrutor.setEspecialidade(dto.getEspecialidade());
        
        return instrutorRepository.save(instrutor);
    }

    // UPDATE
    public Instrutor atualizar(Long id, InstrutorRequestDTO dto) {
        return buscarPorId(id).map(instrutorExistente -> {
            
            // Lógica de Negócio: Se o email mudou, verificar unicidade
            if (!instrutorExistente.getEmail().equals(dto.getEmail())) {
                 if (instrutorRepository.findByEmail(dto.getEmail()).isPresent()) {
                    throw new RegraDeNegocioException("Novo e-mail já cadastrado para outro instrutor.");
                }
            }
            
            instrutorExistente.setNome(dto.getNome());
            instrutorExistente.setEmail(dto.getEmail());
            instrutorExistente.setEspecialidade(dto.getEspecialidade());
            
            return instrutorRepository.save(instrutorExistente);
        }).orElseThrow(() -> new RecursoNaoEncontradoException("Instrutor não encontrado com ID: " + id));
    }
    
    // READ
    public Optional<Instrutor> buscarPorId(Long id) {
        return instrutorRepository.findById(id);
    }
    
    // DELETE
    public void deletarPorId(Long id) {
        if (!instrutorRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Instrutor não encontrado com ID: " + id);
        }
        instrutorRepository.deleteById(id);
    }
    
    // Outros métodos READ...
    public List<Instrutor> buscarTodos() {
        return instrutorRepository.findAll();
    }
    public List<Instrutor> buscarPorEspecialidade(String especialidade) {
        return instrutorRepository.findByEspecialidadeContainingIgnoreCase(especialidade);
    }
}