package com.idutra.api.controller;

import com.idutra.api.exception.ObjetoNaoEncontradoException;
import com.idutra.api.model.dto.rest.Personagem;
import com.idutra.api.model.dto.rest.PersonagemDTO;
import com.idutra.api.model.dto.rest.request.CriarPersonagemRequestDTO;
import com.idutra.api.model.dto.rest.request.ListarPersonagemRequestDTO;
import com.idutra.api.model.dto.rest.response.CriarPersonagemResponseDTO;
import com.idutra.api.repository.PersonagemRepository;
import com.idutra.api.service.PersonagemService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.idutra.api.constants.MensagemConstant.URI_BASE;
import static com.idutra.api.constants.MensagemConstant.URI_CHAR;
import static com.idutra.api.constants.MensagemConstant.URI_CHAR_CREATE;
import static com.idutra.api.constants.MensagemConstant.URI_CHAR_GET;
import static com.idutra.api.constants.MensagemConstant.URI_CHAR_REMOVE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
@SpringBootTest
class HarryPotterControllerTest extends AbstractControllerTest {

    @Autowired
    private PersonagemService service;

    @Autowired
    private PersonagemRepository repository;

    @BeforeEach
    private void setUp() {
        this.repository.deleteAll();
        super.iniciarWireMockServerPotterApi();
    }

    @AfterEach
    private void finalizar() {
        this.wireMockServer.stop();
    }

    @Test
    void buscarPersonagem() {
        Personagem personagem = this.getPersonagemMock();
        this.mockResponseGeneric(this.getCharactersApiMock(personagem), HttpMethod.GET, REGEX_GET_CHAR_POTTER_API, HttpStatus.CREATED);
        this.mockResponseGeneric(this.getHouseApiMock(personagem.getHouseId(), personagem.getHouseId()), HttpMethod.GET, REGEX_GET_HOUSE_API, HttpStatus.CREATED);
        PersonagemDTO personagemDTO = this.modelMapper.map(personagem, PersonagemDTO.class);
        CriarPersonagemResponseDTO responseDTO = assertDoesNotThrow(() -> this.service.salvarPersonagem(personagemDTO));
        assertNotNull(responseDTO);
        assertDoesNotThrow(() -> this.mockMvc.perform(get(URI_BASE.concat(URI_CHAR_GET), responseDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.school").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.houseId").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.patronus").isNotEmpty()).andReturn());
    }

    @Test
    void buscarPersonagemNotFound() {
        assertDoesNotThrow(() -> this.mockMvc.perform(get(URI_BASE.concat(URI_CHAR_GET), RandomStringUtils.random(8, true, true))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound()).andReturn());
    }

    @Test
    void salvarPersonagem() {
        Personagem personagem = this.getPersonagemMock();
        CriarPersonagemRequestDTO requestDTO = this.modelMapper.map(personagem, CriarPersonagemRequestDTO.class);
        this.mockResponseGeneric(this.getCharactersApiMock(personagem), HttpMethod.GET, REGEX_GET_CHAR_POTTER_API, HttpStatus.CREATED);
        this.mockResponseGeneric(this.getHouseApiMock(personagem.getHouseId(), personagem.getHouseId()), HttpMethod.GET, REGEX_GET_HOUSE_API, HttpStatus.CREATED);
        assertDoesNotThrow(() -> this.mockMvc.perform(post(URI_BASE.concat(URI_CHAR_CREATE))
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(requestDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.school").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.houseId").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.patronus").isNotEmpty()).andReturn());
    }

    @Test
    void salvarPersonsagemBadRequest() {
        Personagem personagem = this.getPersonagemMock();
        CriarPersonagemRequestDTO requestDTO = this.modelMapper.map(personagem, CriarPersonagemRequestDTO.class);

        assertDoesNotThrow(() -> this.mockMvc.perform(post(URI_BASE.concat(URI_CHAR_CREATE))
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(requestDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest()).andReturn());
    }

    @Test
    void removerPersonagem() {
        Personagem personagem = this.getPersonagemMock();
        this.mockResponseGeneric(this.getCharactersApiMock(personagem), HttpMethod.GET, REGEX_GET_CHAR_POTTER_API, HttpStatus.CREATED);
        this.mockResponseGeneric(this.getHouseApiMock(personagem.getHouseId(), personagem.getHouseId()), HttpMethod.GET, REGEX_GET_HOUSE_API, HttpStatus.CREATED);
        PersonagemDTO personagemDTO = this.modelMapper.map(personagem, PersonagemDTO.class);
        CriarPersonagemResponseDTO responseDTO = assertDoesNotThrow(() -> this.service.salvarPersonagem(personagemDTO));
        assertNotNull(responseDTO);

        assertDoesNotThrow(() -> this.mockMvc.perform(delete(URI_BASE.concat(URI_CHAR_REMOVE), responseDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem").isNotEmpty()).andReturn());

        assertThrows(ObjetoNaoEncontradoException.class, () -> this.service.consultarPersonagem(responseDTO.getId()));
    }

    @Test
    void listarPersonagens() {
        Personagem personagem = this.getPersonagemMock();
        this.mockResponseGeneric(this.getCharactersApiMock(personagem), HttpMethod.GET, REGEX_GET_CHAR_POTTER_API, HttpStatus.CREATED);
        this.mockResponseGeneric(this.getHouseApiMock(personagem.getHouseId(), personagem.getHouseId()), HttpMethod.GET, REGEX_GET_HOUSE_API, HttpStatus.CREATED);
        PersonagemDTO personagemDTO = this.modelMapper.map(personagem, PersonagemDTO.class);
        CriarPersonagemResponseDTO responseDTO = assertDoesNotThrow(() -> this.service.salvarPersonagem(personagemDTO));
        assertNotNull(responseDTO);

        ListarPersonagemRequestDTO listarPersonagemRequestDTO = new ListarPersonagemRequestDTO();
        listarPersonagemRequestDTO.setName(responseDTO.getName());

        assertDoesNotThrow(() -> this.mockMvc.perform(get(URI_BASE.concat(URI_CHAR))
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(listarPersonagemRequestDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.personagens").isNotEmpty()).andReturn());

    }


}
