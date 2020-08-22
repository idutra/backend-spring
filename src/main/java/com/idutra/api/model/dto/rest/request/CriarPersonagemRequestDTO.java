package com.idutra.api.model.dto.rest.request;

import com.idutra.api.model.dto.rest.PersonagemDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(type = "object", description = "Representação do request de criar personagem.")
public class CriarPersonagemRequestDTO extends PersonagemDTO {

}
