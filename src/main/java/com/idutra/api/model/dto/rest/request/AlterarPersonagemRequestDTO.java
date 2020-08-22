package com.idutra.api.model.dto.rest.request;

import com.idutra.api.model.dto.rest.PersonagemDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AlterarPersonagemRequestDTO extends PersonagemDTO {
    @NotEmpty
    @Schema(description = "Código identificador único do personagem", type = "string", required = true)
    private String uuid;
}
