package com.br.api_agendamento.config.security;

import com.br.api_agendamento.model.Usuario;
import com.br.api_agendamento.repositories.UsuarioRepository;
import com.br.api_agendamento.services.jwt.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private  UsuarioRepository usuarioRepository;

    

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. Recupera o token do cabeçalho da requisição
        String token = tokenService.recoverToken(request.getHeader("Authorization"));

        if (token != null) {
            try {
                // 2. Extrai o "subject" (o email) do token
                String subject = tokenService.getSubject(token);
                
                // 3. Busca o usuário no banco de dados
                Usuario usuario = usuarioRepository.findByEmail(subject)
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado no token JWT"));

                // 4. Cria o objeto de autenticação para o Spring Security
                // Usa o objeto Usuario (UserDetails) e suas permissões (getAuthorities())
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

                // 5. Define o usuário como autenticado no contexto do Spring Security
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                // Em caso de token inválido, expirado ou erro, limpa o contexto
                SecurityContextHolder.clearContext();
                // Opcionalmente, você pode querer definir um status de erro na resposta aqui
            }
        }

        // Continua o fluxo da requisição para o próximo filtro (ou para o Controller)
        filterChain.doFilter(request, response);
    }
}