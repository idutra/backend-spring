package com.idutra.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.idutra.api.exception.ObjetoNaoEncontradoException;
import com.idutra.api.exception.ValidacaoNegocioException;
import com.idutra.api.model.dto.rest.Personagem;
import com.idutra.api.model.dto.rest.PersonagemDTO;
import com.idutra.api.model.dto.rest.request.AlterarPersonagemRequestDTO;
import com.idutra.api.model.dto.rest.request.ListarPersonagemRequestDTO;
import com.idutra.api.model.dto.rest.response.AtualizarPersonagemResponseDTO;
import com.idutra.api.model.dto.rest.response.CriarPersonagemResponseDTO;
import com.idutra.api.model.dto.rest.response.DeleteResponseDTO;
import com.idutra.api.model.dto.rest.response.ListarPersonagemResponseDTO;
import com.idutra.api.model.dto.rest.response.PersonagemResponseDTO;
import com.idutra.api.repository.PersonagemRepository;
import com.idutra.api.service.hpapi.HarryPotterApiService;
import com.idutra.api.service.hpapi.model.CharactersApiDTO;
import com.idutra.api.service.hpapi.model.HouseApiDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.idutra.api.constants.LiteralConstants.PERSONAGEM_CACHE;
import static com.idutra.api.constants.MensagemConstant.MSG_INT_POTTER_API_CHAR_HOUSE_INVALID;
import static com.idutra.api.constants.MensagemConstant.MSG_LISTA_PERSONAGEM_EMPTY;
import static com.idutra.api.constants.MensagemConstant.MSG_PERSONAGEM_DUPLICADO;
import static com.idutra.api.constants.MensagemConstant.MSG_PERSONAGEM_ID_NOT_EMPTY;
import static com.idutra.api.constants.MensagemConstant.MSG_PERSONAGEM_NOT_FOUND;
import static com.idutra.api.constants.MensagemConstant.MSG_PERSONAGEM_NOT_NULL;
import static com.idutra.api.constants.MensagemConstant.MSG_REQUEST_NOT_NULL;
import static com.idutra.api.constants.MensagemConstant.MSG_UPDATE_PERSONAGEM_HOUSE_ERROR;
import static com.idutra.api.constants.MensagemConstant.MSG_UPDATE_PERSONAGEM_NAME_ERROR;

@Log4j2
@Service
@Validated
public class PersonagemService extends GenericService<PersonagemRepository, Personagem, String> {


    private HarryPotterApiService hpApiService;

    @Autowired
    public PersonagemService(PersonagemRepository repository, HarryPotterApiService hpApiService) {
        super(repository);
        this.hpApiService = hpApiService;
    }

    /**
     * Método responsável pela inclusão de um novo personagem.
     * Como requisito para incluir um novo personagem se faz necessário validar se o personagem existe e se está sendo vinculado a casa correta.
     *
     * @param personagemDTO
     * @return
     * @throws ValidacaoNegocioException
     * @throws JsonProcessingException
     */
    @Transactional
    public CriarPersonagemResponseDTO salvarPersonagem(@Valid @NotNull(message = MSG_REQUEST_NOT_NULL) PersonagemDTO personagemDTO) {
        log.info("Iniciando processo para salvar personagem [{}]", personagemDTO.getName());
        Personagem personagem = this.instanceModelMapper(null).map(personagemDTO, Personagem.class);
        this.validarInclusaoPersonagem(personagem);
        log.info("salvando o personagem");
        this.repository.save(personagem);
        log.info("personagem salvo com sucesso...");
        return this.instanceModelMapper(null).map(personagem, CriarPersonagemResponseDTO.class);
    }

    /**
     * Método responsável por realizar a atualização de um personagem já existente com base no resquest
     * Só é permitido atualizar as informações de: patronus,role,school
     *
     * @param personagemDTO
     * @return
     */
    @Transactional
    @CacheEvict(value = PERSONAGEM_CACHE, beforeInvocation = true, allEntries = true)
    public AtualizarPersonagemResponseDTO atualizarPersonagem(@Valid @NotNull(message = MSG_REQUEST_NOT_NULL) AlterarPersonagemRequestDTO personagemDTO) {
        Personagem personagem = this.repository.findById(personagemDTO.getId()).map(p -> {
            log.info("Validando as informações a serem alteradas");
            if (!p.getName().equals(personagemDTO.getName())) {
                throw new ValidacaoNegocioException(MSG_UPDATE_PERSONAGEM_NAME_ERROR, p.getName());
            }
            if (!p.getHouseId().equals(personagemDTO.getHouseId())) {
                throw new ValidacaoNegocioException(MSG_UPDATE_PERSONAGEM_HOUSE_ERROR, p.getHouseId());
            }
            log.info("Alterando os valores do personagem");
            p.setPatronus(personagemDTO.getPatronus());
            p.setRole(personagemDTO.getRole());
            p.setSchool(personagemDTO.getSchool());
            p.setDataHoraUltAtualizacao(OffsetDateTime.now());
            return p;
        }).orElseThrow(() -> new ObjetoNaoEncontradoException(MSG_PERSONAGEM_NOT_FOUND, personagemDTO.getId()));
        log.info("Atualizando as informações do personagem");
        this.repository.save(personagem);
        log.info("Personagem {} uuid {} atualizado...", personagem.getName(), personagemDTO.getId());
        return this.instanceModelMapper(null).map(personagem, AtualizarPersonagemResponseDTO.class);
    }

    /**
     * Método responsável por validar a inclusão de um novo personagem na base interna
     * Este faz chamada de consulta a api Harry Potter Api para obter informações do Personagem e de Casa
     *
     * @param personagem
     * @throws JsonProcessingException
     */
    protected void validarInclusaoPersonagem(@NotNull(message = MSG_PERSONAGEM_NOT_NULL) Personagem personagem) {
        CharactersApiDTO charactersApiDTO = hpApiService.consultarPersonagemApi(personagem);
        HouseApiDTO houseApiDTO = hpApiService.consultarCasaApi(personagem.getHouseId());
        if (!charactersApiDTO.getHouse().equals(houseApiDTO.getName())) {
            throw new ValidacaoNegocioException(MSG_INT_POTTER_API_CHAR_HOUSE_INVALID, personagem.getName(), personagem.getHouseId(), houseApiDTO.get__v());
        }
        personagem.setId(charactersApiDTO.get_id());
        this.repository.findById(personagem.getId()).ifPresent(p -> {
            throw new ValidacaoNegocioException(MSG_PERSONAGEM_DUPLICADO, p.getName(), p.getId());
        });
    }

    /**
     * Método responsável por remover um personagem com base no parâmetro informado
     *
     * @param id
     */
    @Transactional
    @CacheEvict(value = PERSONAGEM_CACHE, beforeInvocation = true, allEntries = true)
    public DeleteResponseDTO removerPersonagem(@NotEmpty(message = MSG_PERSONAGEM_ID_NOT_EMPTY) String id) {
        log.info("Iniciando a exclusão do personagem uuid {}", id);
        Personagem personagem = this.repository.findById(id).orElseThrow(() -> new ObjetoNaoEncontradoException(MSG_PERSONAGEM_NOT_FOUND, id));
        this.repository.delete(personagem);
        log.info("Operação realizada com sucesso..");
        return this.instanceModelMapper(null).map(personagem,DeleteResponseDTO.class);
    }

    /**
     * Método responsável por retornar uma listage de personagem com base no filtro informado
     *
     * @param personagemDTO
     * @return
     */
    public ListarPersonagemResponseDTO listarPersonagens(ListarPersonagemRequestDTO personagemDTO) {
        Personagem personagem = this.instanceModelMapper(null).map(personagemDTO, Personagem.class);
        log.info("Iniciando a pesquisa de personagen com o filtro {} ", personagem.toString());
        Example<Personagem> example = Example.of(personagem);
        List<Personagem> pList = (List<Personagem>) this.repository.findAll(example);
        log.info("{} personagens encontrados", pList.size());
        ListarPersonagemResponseDTO listarPersonagemDTO = new ListarPersonagemResponseDTO();
        List<PersonagemResponseDTO> dtoList = pList.stream().map(p -> this.instanceModelMapper(null).map(p, PersonagemResponseDTO.class)).collect(Collectors.toList());
        if (dtoList.isEmpty()) {
            throw new ObjetoNaoEncontradoException(MSG_LISTA_PERSONAGEM_EMPTY);
        }
        listarPersonagemDTO.setPersonagens(dtoList);
        log.info("Listagem de personagens finalizada com sucesso");
        return listarPersonagemDTO;
    }

    /**
     * Método responsável por retornar um personagem com base no parâmetro informado
     *
     * @param id
     * @return
     */
    @Cacheable(PERSONAGEM_CACHE)
    public PersonagemDTO consultarPersonagem(@NotEmpty(message = MSG_PERSONAGEM_ID_NOT_EMPTY) String id) {
        log.info("Iniciando a pesquisa pelo personagem código {}", id);
        Personagem personagem = this.repository.findById(id).orElseThrow(() -> new ObjetoNaoEncontradoException(MSG_PERSONAGEM_NOT_FOUND, id));
        log.info("Consulta realizada com sucesso..");
        return this.instanceModelMapper(null).map(personagem, PersonagemDTO.class);
    }
}
