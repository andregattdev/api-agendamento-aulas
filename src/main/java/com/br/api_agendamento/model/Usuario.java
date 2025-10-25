package com.br.api_agendamento.model;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

// IMPORTS NECESSÁRIOS DO SPRING SECURITY
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
// ------------------------------------

@Data
@Entity
// 1. ADICIONAR A IMPLEMENTAÇÃO
public class Usuario implements UserDetails { // <--- CORREÇÃO AQUI

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String email;

    private String telefone;

    // 2. ADICIONAR ANOTAÇÃO PARA IGNORAR SENHA NO JSON
    @JsonIgnore // Garante que a senha criptografada não retorne na API
    private String senha;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipo = TipoUsuario.CLIENTE;

    public Usuario() {
    }

    // Construtor completo omitido aqui por brevidade

    // 3. IMPLEMENTAÇÃO DOS MÉTODOS OBRIGATÓRIOS DO USERDETAILS
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Mapeamos o Enum TipoUsuario para a permissão do Spring Security
        // (ROLE_CLIENTE)
        return List.of(new SimpleGrantedAuthority("ROLE_" + tipo.name()));
    }

    // Usamos o campo 'email' como o 'username' para o Spring Security
    @Override
    public String getUsername() {
        return email;
    }

    // Retorna a senha criptografada
    @Override
    public String getPassword() {
        return senha;
    }

    // Assumimos que a conta nunca expira
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Assumimos que a conta nunca está bloqueada
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Assumimos que as credenciais nunca expiram
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Assumimos que o usuário está sempre habilitado
    @Override
    public boolean isEnabled() {
        return true;
    }
}