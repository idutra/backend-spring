package com.idutra.api.service;

import com.idutra.api.exception.ObjetoNaoEncontradoException;
import com.idutra.api.exception.ValidacaoNegocioException;
import com.idutra.api.model.dto.rest.Personagem;
import com.idutra.api.model.dto.rest.PersonagemDTO;
import com.idutra.api.model.dto.rest.request.AlterarPersonagemRequestDTO;
import com.idutra.api.model.dto.rest.response.AtualizarPersonagemResponseDTO;
import com.idutra.api.model.dto.rest.response.ConsultarPersonagemResponseDTO;
import com.idutra.api.model.dto.rest.response.CriarPersonagemResponseDTO;
import com.idutra.api.repository.PersonagemRepository;
import com.idutra.api.service.hpapi.HarryPotterApiService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
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

@Log4j2
@Service
@Validated
public class PersonagemService extends GenericService<PersonagemRepository, Personagem, String> {

    private HarryPotterApiService hpApiService;

    @Autowired
    public PersonagemService(PersonagemRepository repository,
                             HarryPotterApiService hpApiService) {
        super(repository);
        this.hpApiService = hpApiService;
    }

    @Transactional
    public CriarPersonagemResponseDTO salvarPersonagem(@Valid @NotNull PersonagemDTO personagemDTO) {
        log.info("Iniciando processo para salvar personagem [{}]", personagemDTO.getName());
        Personagem personagem = this.instanceModelMapper(null).map(personagemDTO, Personagem.class);
        this.verificarCasaPersonagem(personagem.getHouseId());
        log.info("salvando o personagem");
        this.repository.save(personagem);
        log.info("personagem salvo com sucesso...");
        return this.instanceModelMapper(null).map(personagem, CriarPersonagemResponseDTO.class);
    }

    @Transactional
    public AtualizarPersonagemResponseDTO atualizarPersonagem(@Valid @NotNull AlterarPersonagemRequestDTO personagemDTO) {
        Personagem personagem = this.repository.findPersonagemByUuidAndName(personagemDTO.getUuid(), personagemDTO.getName()).map(p -> {
            log.info("Validando as informações a serem alteradas");
            if (!p.getName().equals(personagemDTO.getName())) {
                throw new ValidacaoNegocioException("", p.getName());
            }
            this.verificarCasaPersonagem(personagemDTO.getHouseId());
            log.info("Alterando os valores do personagem");
            p.setHouseId(personagemDTO.getHouseId());
            p.setPatronus(personagemDTO.getPatronus());
            p.setRole(personagemDTO.getRole());
            p.setSchool(personagemDTO.getSchool());
            p.setDataHoraUltAtualizacao(OffsetDateTime.now());
            return p;
        }).orElseThrow(() -> new ObjetoNaoEncontradoException("", personagemDTO.getUuid()));
        log.info("Atualizando as informações do personagem");
        this.repository.save(personagem);
        log.info("Personagem {} uuid {} atualizado...", personagemDTO.getName(), personagemDTO.getUuid());
        return this.instanceModelMapper(null).map(personagem, AtualizarPersonagemResponseDTO.class);
    }

    private void verificarCasaPersonagem(@NotEmpty String codigoCasa) {
        hpApiService.consultarCasaPersonagem(codigoCasa);
    }

    @Transactional
    public String removerPersonagem(String codigoUuid) {
        log.info("Iniciando a exclusão do personagem uuid {}", codigoUuid);
        Personagem personagem = this.repository.findById(codigoUuid).orElseThrow(() -> new ObjetoNaoEncontradoException("", codigoUuid));
        this.repository.delete(personagem);
        log.info("Operação realizada com sucesso..");
        return String.format("Personagem {%s}, uuid {%s} excluído", personagem.getName(), personagem.getUuid());
    }

    public ConsultarPersonagemResponseDTO consultarPersonagens(String name, String role, String school, String houseId, String patronus, String uuid) {
        Personagem personagem = new Personagem(name, role, school, houseId, patronus, uuid);
        Example<Personagem> example = Example.of(personagem);
        List<Personagem> pList = (List<Personagem>) this.repository.findAll(example);
        ConsultarPersonagemResponseDTO consultarPersonagemResponseDTO = new ConsultarPersonagemResponseDTO();
        List<PersonagemDTO> dtoList = pList.stream().map(p -> {
            return this.instanceModelMapper(null).map(p, PersonagemDTO.class);
        }).collect(Collectors.toList());
        consultarPersonagemResponseDTO.setPersonagens(dtoList);
        return consultarPersonagemResponseDTO;
    }
}
