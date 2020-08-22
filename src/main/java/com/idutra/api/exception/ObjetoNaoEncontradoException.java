package com.idutra.api.exception;

public class ObjetoNaoEncontradoException extends AbstractException {
    public ObjetoNaoEncontradoException(String message) {
        super(message);
    }
    public ObjetoNaoEncontradoException(String message, String... args) {
        super(message, args);
    }
}
