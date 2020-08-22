package com.idutra.api.service.hpapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.idutra.api.exception.IntegracaoApiHpException;
import com.idutra.api.service.hpapi.model.ErroDTO;
import com.idutra.api.service.hpapi.model.HouseApiDTO;
import com.idutra.api.service.hpapi.model.HouseErroDTO;
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
import java.util.Optional;

import static com.idutra.api.constants.MensagemConstant.MSG_INT_POTTER_API_ERROR;
import static com.idutra.api.constants.MensagemConstant.MSG_INT_POTTER_API_HOUSE_NOT_FOUND;

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

    public void consultarCasaPersonagem(@NotEmpty String houseId) {
        log.info("Iniciando a comunicação com a api para consultar a casa [{}]", houseId);
        try {
            Response response = this.getHouseResponse(houseId);
            this.validarReponse(response);
        } catch (ProcessingException ex) {
            log.debug("Falha na integração com a API PotterApi: {}", ExceptionUtils.getRootCauseMessage(ex));
            throw new IntegracaoApiHpException(ExceptionUtils.getRootCauseMessage(ex));
        }
        log.info("Integração finalizada...");
    }

    private Response getHouseResponse(String houseId) {
        return this.harryPotterApi.getHouseById(this.key, houseId);
    }

    private void validarReponse(@NotNull Response response) {
        try {
            Preconditions.checkNotNull(response);
            HttpStatus status = HttpStatus.valueOf(response.getStatus());
            switch (status) {
                case CREATED:
                case OK:
                    String json = response.readEntity(String.class);
                    try {
                        Optional<HouseApiDTO> houseApiDTO = Arrays.stream(objectMapper.readValue(json, HouseApiDTO[].class)).findFirst();
                        log.info(houseApiDTO.get().toJson());
                    } catch (JsonProcessingException e) {
                        HouseErroDTO erroDTO = objectMapper.readValue(json, HouseErroDTO.class);
                        throw new IntegracaoApiHpException(MSG_INT_POTTER_API_HOUSE_NOT_FOUND, new Throwable(erroDTO.toJson()), erroDTO.getValue());
                    }
                    break;
                default:
                    ErroDTO erro = response.readEntity(ErroDTO.class);
                    throw new IntegracaoApiHpException(MSG_INT_POTTER_API_ERROR, erro);
            }
        } catch (ProcessingException | JsonProcessingException ex) {
            log.debug("Falha na integração com a API PotterApi: {}", ExceptionUtils.getRootCauseMessage(ex));
            throw new IntegracaoApiHpException(ExceptionUtils.getRootCauseMessage(ex));
        }
    }
}
