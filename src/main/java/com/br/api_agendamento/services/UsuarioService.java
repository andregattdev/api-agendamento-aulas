package com.br.api_agendamento.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.api_agendamento.model.Usuario;
import com.br.api_agendamento.repositories.UsuarioRepository;

@Service
public class UsuarioService {

    
    @Autowired
    private  UsuarioRepository usuarioRepository;


    // CREATE (Cria/Salva um novo Usuário)
    public Usuario salvar(Usuario usuario) {
        // Implementar lógica de negócio aqui (ex: validar email)
        return usuarioRepository.save(usuario);
    }

    // READ (Busca todos os Usuários)
    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAll();
    }

    // READ (Busca Usuário por ID)
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    // UPDATE (Atualiza um Usuário) - Reusa o método salvar
    public Usuario atualizar(Long id, Usuario usuarioAtualizado) {
        return buscarPorId(id).map(usuarioExistente -> {
            // Regra: Não se altera o ID
            usuarioExistente.setNome(usuarioAtualizado.getNome());
            usuarioExistente.setEmail(usuarioAtualizado.getEmail());
            usuarioExistente.setTelefone(usuarioAtualizado.getTelefone());
            
            // Salva e retorna a entidade atualizada
            return usuarioRepository.save(usuarioExistente);
        }).orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id)); 
        // Em um projeto real, seria uma exceção HTTP 404 customizada
    }

    // DELETE (Deleta Usuário por ID)
    public void deletarPorId(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado com ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }
}
