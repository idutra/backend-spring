package com.idutra.api.service;

import com.idutra.api.AbstractContextTest;
import com.idutra.api.exception.ObjetoNaoEncontradoException;
import com.idutra.api.exception.ValidacaoNegocioException;
import com.idutra.api.model.dto.rest.Personagem;
import com.idutra.api.model.dto.rest.PersonagemDTO;
import com.idutra.api.model.dto.rest.request.AlterarPersonagemRequestDTO;
import com.idutra.api.model.dto.rest.request.ListarPersonagemRequestDTO;
import com.idutra.api.model.dto.rest.response.AtualizarPersonagemResponseDTO;
import com.idutra.api.model.dto.rest.response.CriarPersonagemResponseDTO;
import com.idutra.api.model.dto.rest.response.ListarPersonagemResponseDTO;
import com.idutra.api.repository.PersonagemRepository;
import joptsimple.internal.Strings;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class PersonagemServiceTest extends AbstractContextTest {

    @Autowired
    private PersonagemService service;
    @Autowired
    private PersonagemRepository repository;

    @BeforeEach
    private void setUp() {
        this.repository.deleteAll();
        super.iniciarWireMockServerPotterApi();
    }

    @Test
    void salvarPersonagem() {
        Personagem personagem = this.getPersonagemMock();
        this.mockResponseGeneric(this.getCharactersApiMock(personagem), HttpMethod.GET, REGEX_GET_CHAR_POTTER_API, HttpStatus.CREATED);
        this.mockResponseGeneric(this.getHouseApiMock(personagem.getHouseId(), personagem.getHouseId()), HttpMethod.GET, REGEX_GET_HOUSE_API, HttpStatus.CREATED);
        PersonagemDTO personagemDTO = this.modelMapper.map(personagem, PersonagemDTO.class);
        CriarPersonagemResponseDTO responseDTO = assertDoesNotThrow(() -> this.service.salvarPersonagem(personagemDTO));
        assertNotNull(responseDTO);
        assertNotNull(this.service.consultarPersonagem(responseDTO.getId()));
    }

    @Test
    void salvarPersonagemDTOInvalid() {
        PersonagemDTO personagemDTO = null;
        assertThrows(ConstraintViolationException.class, () -> this.service.salvarPersonagem(personagemDTO));
    }

    @Test
    void listarPersonagemEmpty() {
        assertThrows(ObjetoNaoEncontradoException.class, () -> this.service.listarPersonagens(new ListarPersonagemRequestDTO()));
    }

    @Test
    void listarPersonagensFiltroExists() {
        Personagem personagem = this.inserirPersonagemMock();
        //Listar Todos os Personagens da Base
        ListarPersonagemResponseDTO responseList = assertDoesNotThrow(() -> this.service.listarPersonagens(new ListarPersonagemRequestDTO()));
        assertTrue(!responseList.getPersonagens().isEmpty());

        //Filtrar por Id
        ListarPersonagemRequestDTO filtroIdDTO = new ListarPersonagemRequestDTO();
        filtroIdDTO.setId(personagem.getId());
        responseList = assertDoesNotThrow(() -> this.service.listarPersonagens(new ListarPersonagemRequestDTO()));
        assertTrue(!responseList.getPersonagens().isEmpty());

        //Filtrar por HouseId
        ListarPersonagemRequestDTO filtroHouseIdDTO = new ListarPersonagemRequestDTO();
        filtroHouseIdDTO.setHouseId(personagem.getHouseId());
        responseList = assertDoesNotThrow(() -> this.service.listarPersonagens(filtroHouseIdDTO));
        assertTrue(!responseList.getPersonagens().isEmpty());

        //Filtrar por Name
        ListarPersonagemRequestDTO filtroNameDTO = new ListarPersonagemRequestDTO();
        filtroNameDTO.setName(personagem.getName());
        responseList = assertDoesNotThrow(() -> this.service.listarPersonagens(filtroNameDTO));
        assertTrue(!responseList.getPersonagens().isEmpty());

        //Filtrar por Patronus
        ListarPersonagemRequestDTO filtroPatronusDTO = new ListarPersonagemRequestDTO();
        filtroPatronusDTO.setPatronus(personagem.getPatronus());
        responseList = assertDoesNotThrow(() -> this.service.listarPersonagens(filtroPatronusDTO));
        assertTrue(!responseList.getPersonagens().isEmpty());

        //Filtrar por Role
        ListarPersonagemRequestDTO filtroRoleDTO = new ListarPersonagemRequestDTO();
        filtroRoleDTO.setRole(personagem.getRole());
        responseList = assertDoesNotThrow(() -> this.service.listarPersonagens(filtroRoleDTO));
        assertTrue(!responseList.getPersonagens().isEmpty());

        //Filtrar por School
        ListarPersonagemRequestDTO filtroSchoolDTO = new ListarPersonagemRequestDTO();
        filtroSchoolDTO.setSchool(personagem.getSchool());
        responseList = assertDoesNotThrow(() -> this.service.listarPersonagens(filtroSchoolDTO));
        assertTrue(!responseList.getPersonagens().isEmpty());
    }

    @Test
    void consultarPersonagemSucesso() {
        Personagem personagem = this.inserirPersonagemMock();
        PersonagemDTO responseDTO = assertDoesNotThrow(() -> this.service.consultarPersonagem(personagem.getId()));
        assertNotNull(responseDTO);
    }

    @Test
    void consultarPersonagemIdInvalid() {
        assertThrows(ConstraintViolationException.class, () -> this.service.consultarPersonagem(Strings.EMPTY));
        assertThrows(ConstraintViolationException.class, () -> this.service.consultarPersonagem(null));
    }

    @Test
    void consultarPersonagemNotExists() {
        Personagem personagem = inserirPersonagemMock();
        assertNotNull(personagem);
        assertNotNull(this.service.consultarPersonagem(personagem.getId()));
        assertThrows(ObjetoNaoEncontradoException.class, () -> this.service.consultarPersonagem(RandomStringUtils.random(5, true, true)));
    }

    @Test
    void removerPersonagemSucesso() {
        Personagem personagem = inserirPersonagemMock();
        assertNotNull(personagem);
        assertNotNull(this.service.consultarPersonagem(personagem.getId()));
        assertDoesNotThrow(() -> this.service.removerPersonagem(personagem.getId()));
        assertThrows(ObjetoNaoEncontradoException.class, () -> this.service.consultarPersonagem(personagem.getId()));
    }

    @Test
    void removerPersonagemIdInvalido() {
        assertThrows(ConstraintViolationException.class, () -> this.service.removerPersonagem(Strings.EMPTY));
        assertThrows(ConstraintViolationException.class, () -> this.service.removerPersonagem(null));
    }

    @Test
    void removerPersonagemNotExists() {
        Personagem personagem = inserirPersonagemMock();
        assertNotNull(personagem);
        assertNotNull(this.service.consultarPersonagem(personagem.getId()));
        assertThrows(ObjetoNaoEncontradoException.class, () -> this.service.removerPersonagem(RandomStringUtils.random(5, true, true)));
    }

    @Test
    void atualizarPersonagemSucesso() {
        Personagem personagemSaved = inserirPersonagemMock();
        assertNotNull(personagemSaved);
        assertNotNull(this.service.consultarPersonagem(personagemSaved.getId()));

        AlterarPersonagemRequestDTO requestDTO = this.modelMapper.map(personagemSaved, AlterarPersonagemRequestDTO.class);
        requestDTO.setPatronus(RandomStringUtils.random(5, true, true));
        requestDTO.setRole(RandomStringUtils.random(5, true, true));
        requestDTO.setSchool(RandomStringUtils.random(5, true, true));

        AtualizarPersonagemResponseDTO responseDTO = assertDoesNotThrow(() -> this.service.atualizarPersonagem(requestDTO));

        assertNotNull(responseDTO);

        assertFalse(responseDTO.getPatronus().equals(personagemSaved.getPatronus()));
        assertFalse(responseDTO.getRole().equals(personagemSaved.getRole()));
        assertFalse(responseDTO.getSchool().equals(personagemSaved.getSchool()));
        assertFalse(responseDTO.getDataHoraUltAtualizacao().equals(personagemSaved.getDataHoraUltAtualizacao()));
        assertTrue(responseDTO.getDataHoraCriacao().equals(personagemSaved.getDataHoraCriacao()));
        assertTrue(responseDTO.getName().equals(personagemSaved.getName()));
        assertTrue(responseDTO.getId().equals(personagemSaved.getId()));
        assertTrue(responseDTO.getHouseId().equals(personagemSaved.getHouseId()));

    }

    @Test
    void atualizarPersonagemDTOInvalid() {
        Personagem personagemSaved = inserirPersonagemMock();
        assertNotNull(personagemSaved);
        assertNotNull(this.service.consultarPersonagem(personagemSaved.getId()));

        AlterarPersonagemRequestDTO requestDTO = null;
        assertThrows(ConstraintViolationException.class, () -> this.service.atualizarPersonagem(requestDTO));
    }

    @Test
    void atualizarPersonagemIdInvalido() {
        Personagem personagemSaved = inserirPersonagemMock();
        assertNotNull(personagemSaved);
        assertNotNull(this.service.consultarPersonagem(personagemSaved.getId()));

        AlterarPersonagemRequestDTO requestDTO = this.modelMapper.map(personagemSaved, AlterarPersonagemRequestDTO.class);
        requestDTO.setId(Strings.EMPTY);
        assertThrows(ConstraintViolationException.class, () -> this.service.atualizarPersonagem(requestDTO));
        requestDTO.setId(null);
        assertThrows(ConstraintViolationException.class, () -> this.service.atualizarPersonagem(requestDTO));
        requestDTO.setId(RandomStringUtils.random(10, true, true));
        assertThrows(ObjetoNaoEncontradoException.class, () -> this.service.atualizarPersonagem(requestDTO));
    }

    @Test
    void atualizarPersonagemNameInvalid() {
        Personagem personagemSaved = inserirPersonagemMock();
        assertNotNull(personagemSaved);
        assertNotNull(this.service.consultarPersonagem(personagemSaved.getId()));

        AlterarPersonagemRequestDTO requestDTO = this.modelMapper.map(personagemSaved, AlterarPersonagemRequestDTO.class);
        requestDTO.setName(Strings.EMPTY);
        assertThrows(ConstraintViolationException.class, () -> this.service.atualizarPersonagem(requestDTO));
        requestDTO.setName(null);
        assertThrows(ConstraintViolationException.class, () -> this.service.atualizarPersonagem(requestDTO));
        requestDTO.setName(RandomStringUtils.random(15, true, false));
        assertThrows(ValidacaoNegocioException.class, () -> this.service.atualizarPersonagem(requestDTO));
    }

    @Test
    void atualizarPersonagemHouseIdInvalid() {
        Personagem personagemSaved = inserirPersonagemMock();
        assertNotNull(personagemSaved);
        assertNotNull(this.service.consultarPersonagem(personagemSaved.getId()));

        AlterarPersonagemRequestDTO requestDTO = this.modelMapper.map(personagemSaved, AlterarPersonagemRequestDTO.class);
        requestDTO.setHouseId(Strings.EMPTY);
        assertThrows(ConstraintViolationException.class, () -> this.service.atualizarPersonagem(requestDTO));
        requestDTO.setHouseId(null);
        assertThrows(ConstraintViolationException.class, () -> this.service.atualizarPersonagem(requestDTO));
        requestDTO.setHouseId(RandomStringUtils.random(10, true, true));
        assertThrows(ValidacaoNegocioException.class, () -> this.service.atualizarPersonagem(requestDTO));
    }

    @Test
    void atualizarPersonagemSchoolInvalid() {
        Personagem personagemSaved = inserirPersonagemMock();
        assertNotNull(personagemSaved);
        assertNotNull(this.service.consultarPersonagem(personagemSaved.getId()));

        AlterarPersonagemRequestDTO requestDTO = this.modelMapper.map(personagemSaved, AlterarPersonagemRequestDTO.class);
        requestDTO.setSchool(Strings.EMPTY);
        assertThrows(ConstraintViolationException.class, () -> this.service.atualizarPersonagem(requestDTO));
        requestDTO.setSchool(null);
        assertThrows(ConstraintViolationException.class, () -> this.service.atualizarPersonagem(requestDTO));
    }

    @Test
    void atualizarPersonagemRoleInvalid() {
        Personagem personagemSaved = inserirPersonagemMock();
        assertNotNull(personagemSaved);
        assertNotNull(this.service.consultarPersonagem(personagemSaved.getId()));

        AlterarPersonagemRequestDTO requestDTO = this.modelMapper.map(personagemSaved, AlterarPersonagemRequestDTO.class);
        requestDTO.setRole(Strings.EMPTY);
        assertThrows(ConstraintViolationException.class, () -> this.service.atualizarPersonagem(requestDTO));
        requestDTO.setRole(null);
    }

    @Test
    void atualizarPersonagemPatronusInvalid() {
        Personagem personagemSaved = inserirPersonagemMock();
        assertNotNull(personagemSaved);
        assertNotNull(this.service.consultarPersonagem(personagemSaved.getId()));

        AlterarPersonagemRequestDTO requestDTO = this.modelMapper.map(personagemSaved, AlterarPersonagemRequestDTO.class);
        requestDTO.setPatronus(Strings.EMPTY);
        assertThrows(ConstraintViolationException.class, () -> this.service.atualizarPersonagem(requestDTO));
        requestDTO.setPatronus(null);
        assertThrows(ConstraintViolationException.class, () -> this.service.atualizarPersonagem(requestDTO));
    }

    private Personagem inserirPersonagemMock() {
        Personagem personagem = this.getPersonagemMock();
        assertNotNull(this.repository.save(personagem));
        return personagem;
    }
}
