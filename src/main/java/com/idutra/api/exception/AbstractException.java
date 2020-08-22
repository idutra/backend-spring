package com.idutra.api.exception;

import lombok.Getter;

public abstract class AbstractException extends RuntimeException {
    @Getter
    private final String[] args;

    public AbstractException(String mensagem) {
        super(mensagem);
        this.args = null;
    }

    public AbstractException(String message, String[] args) {
        super(message);
        this.args = args;
    }

    public AbstractException(String message, Throwable cause, String[] args) {
        super(message, cause);
        this.args = args;
    }
}
