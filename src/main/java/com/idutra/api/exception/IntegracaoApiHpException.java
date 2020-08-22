package com.idutra.api.exception;

import com.idutra.api.service.hpapi.model.ErroDTO;
import lombok.Getter;

public class IntegracaoApiHpException extends AbstractException {
    @Getter
    private ErroDTO erroDTO;

    public IntegracaoApiHpException(String mensagem) {
        super(mensagem);
    }

    public IntegracaoApiHpException(String message, Throwable cause, String... args) {
        super(message, cause, args);
    }

    public IntegracaoApiHpException(String message, ErroDTO erroDTO) {
        super(message);
        this.erroDTO = erroDTO;
    }
}
