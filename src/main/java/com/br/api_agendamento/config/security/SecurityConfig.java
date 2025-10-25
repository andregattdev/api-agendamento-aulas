package com.br.api_agendamento.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager; // NOVO IMPORT
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; // NOVO IMPORT
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.br.api_agendamento.services.AutenticacaoService;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
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
                        // Vamos liberar o endpoint de login/auth que criaremos em seguida:
                        // .requestMatchers("/auth/**").permitAll() // Se usarmos /auth/login
                        // Mantemos TUDO liberado por enquanto para testar o CRUD
                        .anyRequest().permitAll())
                .httpBasic(withDefaults());

        // 3. ADICIONA O FILTRO JWT antes do filtro padrão de autenticação
        http.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}