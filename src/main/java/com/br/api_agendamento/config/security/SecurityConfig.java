package com.br.api_agendamento.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager; // NOVO IMPORT
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; // NOVO IMPORT
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

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private AutenticacaoService autenticacaoService;

    @Autowired
    private SecurityFilter securityFilter;

    // Injeta o nosso UserDetailsService
    public SecurityConfig(AutenticacaoService autenticacaoService) {
        this.autenticacaoService = autenticacaoService;
        this.securityFilter = securityFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Define o Gerenciador de Autenticação.
     * O Spring Security usa isso para processar as requisições de login.
     */
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
                .authorizeHttpRequests(authorize -> authorize
                        // 1. Libera o login e a criação de usuário para acesso público
                        .requestMatchers("/auth/login", "/usuarios").permitAll()

                        // 2. TODAS as outras rotas exigem que o usuário esteja autenticado (tenha um
                        // token válido)
                        .anyRequest().authenticated())
                // Você também precisa definir a política de sessão como STATELESS para JWT
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // O filtro JWT precisa ser adicionado aqui
        http.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}