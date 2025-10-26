package com.br.api_agendamento.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // NOVO IMPORT NECESSÁRIO
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.br.api_agendamento.services.AutenticacaoService;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private AutenticacaoService autenticacaoService;

    @Autowired
    private SecurityFilter securityFilter;

    // Você pode remover este construtor se usar @Autowired nos campos
    // public SecurityConfig(AutenticacaoService autenticacaoService) {
    //     this.autenticacaoService = autenticacaoService;
    //     this.securityFilter = securityFilter;
    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Configura a cadeia de filtros de segurança (Autorização).
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // 1. Libera o login e a criação de usuário para acesso público
                        .requestMatchers(HttpMethod.POST, "/auth/login", "/usuarios").permitAll()

                        // 🚨 CORREÇÃO PRINCIPAL: Adiciona a regra de autorização para AGENDAMENTO
                        // Somente usuários com o papel CLIENTE podem fazer POST em /agendamentos
                        .requestMatchers(HttpMethod.POST, "/agendamentos").hasRole("CLIENTE")

                        // 2. TODAS as outras rotas exigem que o usuário esteja autenticado (token válido)
                        .anyRequest().authenticated());

        // O filtro JWT precisa ser adicionado aqui
        http.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}