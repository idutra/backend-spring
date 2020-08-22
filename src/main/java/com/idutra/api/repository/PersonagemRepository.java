package com.idutra.api.repository;

import com.idutra.api.model.dto.rest.Personagem;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonagemRepository extends GenericRepository<Personagem, String> {
    Optional<Personagem> findPersonagemByUuidAndName(String uuid, String name);


    List<Personagem> findAllByNameLikeOrRoleLikeOrSchoolLikeOrHouseIdLikeOrPatronusLikeOrUuidLike(String name, String role, String school, String houseId, String patronus, String uuid);
}
