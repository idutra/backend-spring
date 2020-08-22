package com.idutra.api.exception;

public class ValidacaoNegocioException extends AbstractException {

    public ValidacaoNegocioException(String message, String... args) {
        super(message, args);
    }

    public ValidacaoNegocioException(String message, Throwable cause, String[] args) {
        super(message, cause, args);
    }
}
