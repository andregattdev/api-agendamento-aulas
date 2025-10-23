package com.br.api_agendamento.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.api_agendamento.model.TipoUsuario;
import com.br.api_agendamento.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    
    // Método customizado para buscar todos os usuários de um tipo específico (ex: INSTRUTOR)
     List<Usuario> findByTipo(TipoUsuario tipo);

}
