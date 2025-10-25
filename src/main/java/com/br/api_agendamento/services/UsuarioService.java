package com.br.api_agendamento.services;

import com.br.api_agendamento.dto.UsuarioRequestDTO;
import com.br.api_agendamento.exception.RecursoNaoEncontradoException;
import com.br.api_agendamento.exception.RegraDeNegocioException;
import com.br.api_agendamento.model.Usuario;
import com.br.api_agendamento.repositories.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    

    // CREATE
    public Usuario salvar(UsuarioRequestDTO dto) {
        // Regra de Negócio: E-mail deve ser único
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RegraDeNegocioException("E-mail já cadastrado para outro usuário.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefone(dto.getTelefone());
        
        // Se houver campo 'tipo' (Cliente/Admin), defina o tipo padrão aqui.

        return usuarioRepository.save(usuario);
    }

    // UPDATE
    public Usuario atualizar(Long id, UsuarioRequestDTO dto) {
        return buscarPorId(id).map(usuarioExistente -> {
            
            // Regra de Negócio: Se o email mudou, verificar unicidade
            if (!usuarioExistente.getEmail().equals(dto.getEmail())) {
                 if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
                    throw new RegraDeNegocioException("Novo e-mail já cadastrado para outro usuário.");
                }
            }
            
            usuarioExistente.setNome(dto.getNome());
            usuarioExistente.setEmail(dto.getEmail());
            usuarioExistente.setTelefone(dto.getTelefone());
            
            return usuarioRepository.save(usuarioExistente);
        }).orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com ID: " + id));
    }

    // READ ALL
    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAll();
    }

    // READ BY ID
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    // DELETE
    public void deletarPorId(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Usuário não encontrado com ID: " + id);
        }
        // Em um sistema real, aqui você checaria se o usuário tem agendamentos ativos.
        
        usuarioRepository.deleteById(id);
    }
}