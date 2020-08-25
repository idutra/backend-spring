package com.idutra.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.http.HttpHeader;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.idutra.api.model.dto.rest.Personagem;
import com.idutra.api.service.hpapi.model.CharactersApiDTO;
import com.idutra.api.service.hpapi.model.HouseApiDTO;
import com.idutra.api.utils.ApiClientFactory;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.util.MimeTypeUtils;

import java.net.URL;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Log4j2
public abstract class AbstractContextTest {
    protected static String REGEX_GET_CHAR_POTTER_API = "/characters.*";
    protected static String REGEX_GET_HOUSE_API = "/houses/.*";

    @Autowired
    protected ModelMapper modelMapper;
    @Value("${api.url.service}")
    private String urlApi;
    protected WireMockServer wireMockServer;

    protected Personagem getPersonagemMock() {
        Personagem pMock = new Personagem();
        pMock.setPatronus(RandomStringUtils.random(5, true, false));
        pMock.setHouseId(RandomStringUtils.random(8, true, true));
        pMock.setSchool(RandomStringUtils.random(10, true, false));
        pMock.setRole(RandomStringUtils.random(6, true, false));
        pMock.setName(RandomStringUtils.random(10, true, false));
        return pMock;
    }

    protected void iniciarWireMockServerPotterApi() {
        URL url = assertDoesNotThrow(() -> new URL(this.urlApi));
        this.wireMockServer = new WireMockServer(url.getPort());
        this.wireMockServer.start();
        configureFor(url.getHost(), url.getPort());
    }

    protected void mockResponseGeneric(Object response, HttpMethod method, String utlPattern, HttpStatus httpStatus) {
        this.mockResponseGeneric(response, method, utlPattern, httpStatus, null);
    }

    protected void mockResponseGeneric(Object response, HttpMethod method, String utlPattern, HttpStatus httpStatus,
                                       HttpHeaders headers) {
        String responseReturn = response.toString();
        try {
            responseReturn = ApiClientFactory.JSON_MAPPER.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            log.error(e);
        }
        MappingBuilder mappingBuilder = post(urlPathMatching(utlPattern));
        if (HttpMethod.GET.equals(method)) {
            mappingBuilder = get(urlPathMatching(utlPattern));
        } else if (HttpMethod.PUT.equals(method)) {
            mappingBuilder = put(urlPathMatching(utlPattern));
        } else if (HttpMethod.DELETE.equals(method)) {
            mappingBuilder = delete(urlPathMatching(utlPattern));
        }

        final ResponseDefinitionBuilder responseBuilder = this.responseTextJsonMock(httpStatus.value(), responseReturn,
                headers);
        this.wireMockServer.stubFor(mappingBuilder.willReturn(responseBuilder));
    }

    protected ResponseDefinitionBuilder responseTextJsonMock(int status, String body, HttpHeaders headers) {
        if (headers == null) {
            headers = new HttpHeaders();
        }
        headers = headers.plus(HttpHeader.httpHeader("Content-Type", org.assertj.core.util.Arrays.array(MimeTypeUtils.APPLICATION_JSON_VALUE)));
        return aResponse().withStatus(status).withHeaders(headers).withBody(body);
    }

    protected CharactersApiDTO[] getCharactersApiMock(Personagem personagem) {
        CharactersApiDTO charApiMock = this.modelMapper.map(personagem, CharactersApiDTO.class);
        charApiMock.setHouse(personagem.getHouseId());
        charApiMock.set_id(RandomStringUtils.random(8, true, true));
        return new CharactersApiDTO[]{charApiMock};
    }

    protected HouseApiDTO[] getHouseApiMock(String houseId, String houseName) {
        HouseApiDTO houseApiMock = new HouseApiDTO();
        houseApiMock.set_id(houseId);
        houseApiMock.setName(houseName);
        return new HouseApiDTO[]{houseApiMock};
    }
}
