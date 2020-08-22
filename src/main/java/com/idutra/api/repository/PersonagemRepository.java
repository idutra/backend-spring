package com.idutra.api.repository;

import com.idutra.api.model.dto.rest.Personagem;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonagemRepository extends GenericRepository<Personagem, String> {
    Optional<Personagem> findPersonagemByIdAndName(String uuid, String name);
}
