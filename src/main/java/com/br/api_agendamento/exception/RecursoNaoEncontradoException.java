package com.br.api_agendamento.exception;

// Extender RuntimeException permite que não precisemos usar 'throws' nos métodos
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String message) {
        super(message);
    }
}