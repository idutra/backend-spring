package com.idutra.api.service.hpapi;

import com.google.common.base.Preconditions;
import com.idutra.api.utils.ApiClientFactory;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
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

@Log4j2
@Component
@Validated
public class HarryPotterApiService {
    private final String urlApi;
    private final String key;
    private final HarryPotterApi harryPotterApi;
    @Getter
    private final ModelMapper modelMapper;

    @Autowired
    public HarryPotterApiService(final ModelMapper modelMapper,
                                 final @Value("${api.key}") String apiKey,
                                 final @Value("${api.url.service}") String urlApi) {
        this.key = apiKey;
        this.urlApi = urlApi;
        this.harryPotterApi = ApiClientFactory.createApiClient(urlApi, HarryPotterApi.class);
        this.modelMapper = modelMapper;
    }

    public void consultarCasaPersonagem(@NotEmpty String houseId) {
        log.info("Iniciando a comunicação com a api para consultar a casa [{}]", houseId);
        try {
            Response response = this.getHouseResponse(houseId);
            this.validarReponse(response);
        } catch (ProcessingException e) {

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
                    log.info("Response: {}", response.readEntity(String.class));
                    break;
                case SERVICE_UNAVAILABLE:
                    break;
                default:
                    log.debug("");
                    break;
            }
        } catch (ProcessingException ex) {
            log.error("");
        }
    }
}
