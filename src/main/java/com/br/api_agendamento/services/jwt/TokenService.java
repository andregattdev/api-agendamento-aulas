package com.br.api_agendamento.services.jwt;

import com.br.api_agendamento.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expirationTime;

    /**
     * Gera o JWT para o usuário autenticado.
     */
    public String generateToken(Usuario usuario) {
        
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setSubject(usuario.getEmail()) // Identificador do usuário (o email)
                .claim("userId", usuario.getId()) // Informação extra (ID)
                .claim("role", usuario.getTipo().name()) // Informação extra (Role/Tipo)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Obtém o email (subject) do token.
     */
    public String getSubject(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Valida o token e retorna os Claims (dados).
     */
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Cria a chave secreta a partir da string em application.properties.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Retorna apenas o token limpo (sem o prefixo "Bearer ").
     */
    public String recoverToken(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null; // ou lançar exceção, dependendo da sua preferência
    }
}