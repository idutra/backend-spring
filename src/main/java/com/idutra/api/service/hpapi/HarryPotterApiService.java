package com.idutra.api.service.hpapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.idutra.api.exception.IntegracaoApiHpException;
import com.idutra.api.exception.ObjetoNaoEncontradoException;
import com.idutra.api.model.dto.rest.Personagem;
import com.idutra.api.service.hpapi.model.CharactersApiDTO;
import com.idutra.api.service.hpapi.model.ErroDTO;
import com.idutra.api.service.hpapi.model.HouseApiDTO;
import com.idutra.api.utils.ApiClientFactory;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;
import java.util.Arrays;

import static com.idutra.api.constants.MensagemConstant.MSG_INT_POTTER_API_CHAR_NOT_FOUND;
import static com.idutra.api.constants.MensagemConstant.MSG_INT_POTTER_API_ERROR;
import static com.idutra.api.constants.MensagemConstant.MSG_INT_POTTER_API_HOUSE_INVALID;
import static com.idutra.api.constants.MensagemConstant.MSG_PERSONAGEM_HOUSE_ID_NOT_EMPTY;
import static com.idutra.api.constants.MensagemConstant.MSG_PERSONAGEM_NOT_NULL;
import static com.idutra.api.constants.MensagemConstant.MSG_RESPONSE_NOT_NULL;

@Log4j2
@Component
@Validated
public class HarryPotterApiService {
    private final String urlApi;
    private final String key;
    private final HarryPotterApi harryPotterApi;
    @Getter
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

    @Autowired
    public HarryPotterApiService(final ModelMapper modelMapper,
                                 final ObjectMapper objectMapper,
                                 final @Value("${api.key}") String apiKey,
                                 final @Value("${api.url.service}") String urlApi) {
        this.key = apiKey;
        this.urlApi = urlApi;
        this.harryPotterApi = ApiClientFactory.createApiClient(urlApi, HarryPotterApi.class);
        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
    }

    public CharactersApiDTO consultarPersonagemApi(@NotNull(message = MSG_PERSONAGEM_NOT_NULL) Personagem personagem) throws JsonProcessingException {
        log.info("Iniciando a consulta de personagens");
        Response response = this.getCharacter(personagem);
        String jsonRetorno = this.extractReponse(response);
        log.info("Response: [{}]", jsonRetorno);
        return Arrays.stream(objectMapper.readValue(jsonRetorno, CharactersApiDTO[].class)).findFirst().orElseThrow(() -> new ObjetoNaoEncontradoException(MSG_INT_POTTER_API_CHAR_NOT_FOUND, personagem.getName()));
    }

    public HouseApiDTO consultarCasaApi(@NotEmpty(message = MSG_PERSONAGEM_HOUSE_ID_NOT_EMPTY) String houseId) throws JsonProcessingException {
        log.info("Iniciando a comunicação com a api para consultar a casa [{}]", houseId);
        Response response = this.getHouseResponse(houseId);
        String jsonRetorno = this.extractReponse(response);
        log.info("Response: [{}]", jsonRetorno);
        return this.converterResponseHouseApi(jsonRetorno, houseId);
    }

    private Response getCharacter(Personagem personagem) {
        return this.harryPotterApi.getCharacters(this.key, personagem.getName());
    }

    private Response getHouseResponse(String houseId) {
        return this.harryPotterApi.getHouseById(this.key, houseId);
    }

    private HouseApiDTO converterResponseHouseApi(String json, String houseId) {
        try {
            return Arrays.stream(objectMapper.readValue(json, HouseApiDTO[].class)).findFirst().orElseThrow(() -> new ObjetoNaoEncontradoException(MSG_INT_POTTER_API_HOUSE_INVALID, houseId));
        } catch (JsonProcessingException e) {
            log.debug("Ocorreu um erro durante a conversão do json {} para o objeto {}", json, HouseApiDTO.class.getSimpleName());
            throw new IntegracaoApiHpException(MSG_INT_POTTER_API_HOUSE_INVALID, new Throwable(json), houseId);
        }
    }

    private String extractReponse(@NotNull(message = MSG_RESPONSE_NOT_NULL) Response response) {
        try {
            Preconditions.checkNotNull(response);
            HttpStatus status = HttpStatus.valueOf(response.getStatus());
            switch (status) {
                case CREATED:
                case OK:
                    log.debug("Requisição realizada com sucesso [{} - {}]", status.value(), status.getReasonPhrase());
                    return response.readEntity(String.class);
                default:
                    ErroDTO erro = response.readEntity(ErroDTO.class);
                    log.debug("Ocorreu um erro durante a requisição [{} - {}]: [{}]", status.value(), status.getReasonPhrase(), erro.getError());
                    throw new IntegracaoApiHpException(MSG_INT_POTTER_API_ERROR, erro);
            }
        } catch (ProcessingException ex) {
            log.debug(MSG_INT_POTTER_API_ERROR, ExceptionUtils.getRootCauseMessage(ex));
            throw new IntegracaoApiHpException(ExceptionUtils.getRootCauseMessage(ex));
        }
    }
}
