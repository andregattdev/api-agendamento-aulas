package com.br.api_agendamento.controller;

import com.br.api_agendamento.dto.ErroPadraoDTO;
import com.br.api_agendamento.exception.RegraDeNegocioException;
import com.br.api_agendamento.exception.RecursoNaoEncontradoException;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. TRATAMENTO PARA ERROS DE VALIDAÇÃO (@Valid) - HTTP 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroPadraoDTO> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        // Coleta todos os erros de validação (campo + mensagem)
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErroPadraoDTO erro = new ErroPadraoDTO(
                status.value(),
                status.getReasonPhrase(), // "Bad Request"
                "Falha na validação dos dados da requisição.",
                request.getRequestURI(),
                errors
        );
        return new ResponseEntity<>(erro, status);
    }
    
    // 2. TRATAMENTO PARA RECURSO NÃO ENCONTRADO - HTTP 404
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroPadraoDTO> handleRecursoNaoEncontrado(
            RecursoNaoEncontradoException ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;
        ErroPadraoDTO erro = new ErroPadraoDTO(
                status.value(),
                status.getReasonPhrase(), // "Not Found"
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
        return new ResponseEntity<>(erro, status);
    }

    // 3. TRATAMENTO PARA REGRAS DE NEGÓCIO - HTTP 400
    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ErroPadraoDTO> handleRegraDeNegocio(
            RegraDeNegocioException ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErroPadraoDTO erro = new ErroPadraoDTO(
                status.value(),
                status.getReasonPhrase(), // "Bad Request"
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
        return new ResponseEntity<>(erro, status);
    }

    // Você pode adicionar mais handlers para outras exceções (ex: DataIntegrityViolationException)
}