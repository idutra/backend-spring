package com.idutra.api.exception;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.idutra.api.component.MensagemComponente;
import com.idutra.api.model.dto.rest.response.ResponseErroDTO;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Log4j2
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final MensagemComponente mensagemComponente;

    @Autowired
    public ApiExceptionHandler(MensagemComponente mensagemComponente) {
        this.mensagemComponente = mensagemComponente;
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        ResponseErroDTO response;
        if (ex.getCause() != null) {
            if (ex.getCause() instanceof UnrecognizedPropertyException) {
                String nomePropriedade = ((UnrecognizedPropertyException) ex.getCause()).getPropertyName();
                response = new ResponseErroDTO(this.mensagemComponente.get("MSG_PROPRIEDADE_DESCONHECIDA", nomePropriedade),
                        ExceptionUtils.getRootCauseMessage(ex));
            } else {
                response = new ResponseErroDTO(ex.getCause().getMessage(), ExceptionUtils.getRootCauseMessage(ex));
            }
        } else {
            response = new ResponseErroDTO(this.mensagemComponente.get("MSG_REQUEST_BODY_MISSING"),
                    ExceptionUtils.getRootCauseMessage(ex));
        }

        return super.handleExceptionInternal(ex, response, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {

        List<String> mensagensErro = new ArrayList<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            mensagensErro.add(this.mensagemComponente.get(fieldError.getDefaultMessage()));
        }
        ResponseErroDTO response = new ResponseErroDTO(mensagensErro.toString(), ExceptionUtils.getRootCauseMessage(ex));

        return super.handleExceptionInternal(ex, response, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex,
                                                                     HttpHeaders headers, HttpStatus status,
                                                                     WebRequest request) {
        ResponseErroDTO response = new ResponseErroDTO(this.mensagemComponente.get("MSG_REQUEST_PARAM_MISSING"),
                ExceptionUtils.getRootCauseMessage(ex));
        return super.handleExceptionInternal(ex, response, headers, status, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> constraintViolationException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        List<String> mensagensErro = new ArrayList<>();
        for (ConstraintViolation<?> violation : violations) {
            mensagensErro.add(this.mensagemComponente.get(violation.getMessage()));
        }
        ResponseErroDTO response = new ResponseErroDTO(mensagensErro.toString(), ExceptionUtils.getRootCauseMessage(e));
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> missingRequestHeaderException(MissingRequestHeaderException ex) {
        return this.getRespostaErroPadrao("MISSING_HEADER_MESSAGE", ExceptionUtils.getRootCauseMessage(ex));
    }

    @ExceptionHandler({ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleValidationException(ValidationException ex) {
        return this.getRespostaErroPadrao(ex.getMessage(), ExceptionUtils.getRootCauseMessage(ex));
    }

    @ExceptionHandler(ObjetoNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleObjetoNaoEncontradoException(ObjetoNaoEncontradoException exception) {
        return this.getRespostaErroPadrao(HttpStatus.NOT_FOUND, exception.getMessage(), ExceptionUtils.getRootCauseMessage(exception), exception.getArgs());
    }

    @ExceptionHandler(ValidacaoNegocioException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleValidacaoNegocioException(ValidacaoNegocioException ex) {
        return this.getRespostaErroPadrao(HttpStatus.BAD_REQUEST, ex.getMessage(), ExceptionUtils.getRootCauseMessage(ex), ex.getArgs());
    }

    @ExceptionHandler(IntegracaoApiHpException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleIntegracaoApiHpException(IntegracaoApiHpException ex) {
        String mensagemDetalhada = ex.getErroDTO() != null ? ex.getErroDTO().getError() : ExceptionUtils.getRootCauseMessage(ex);
        return this.getRespostaErroPadrao(HttpStatus.BAD_REQUEST, ex.getMessage(), mensagemDetalhada, ex.getArgs());
    }

    private ResponseEntity<Object> getRespostaErroPadrao(String message, String stackMessage, String... params) {
        return this.getRespostaErroPadrao(HttpStatus.BAD_REQUEST, message, stackMessage, params);
    }

    private ResponseEntity<Object> getRespostaErroPadrao(HttpStatus httpStatus, String message, String stackMessage,
                                                         String... params) {
        ResponseErroDTO response;
        if (params == null || params.length == 0) {
            response = new ResponseErroDTO(this.mensagemComponente.get(message), stackMessage);
        } else {
            response = new ResponseErroDTO(this.mensagemComponente.get(message, params), stackMessage);
        }
        log.error(response);
        return ResponseEntity.status(httpStatus).body(response);
    }
}

