package com.idutra.api.model.dto.rest.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(type = "object", description = "Representação do response de listar personagem.")
public class ListarPersonagemResponseDTO {
    List<PersonagemResponseDTO> personagens;
}
