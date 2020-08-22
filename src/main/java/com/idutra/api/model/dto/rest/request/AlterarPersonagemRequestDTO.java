package com.idutra.api.model.dto.rest.request;

import com.idutra.api.model.dto.rest.PersonagemDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

import static com.idutra.api.constants.MensagemConstant.MSG_PERSONAGEM_ID_NOT_EMPTY;

@Data
@Schema(type = "object", description = "Representação do request de alterar.")
public class AlterarPersonagemRequestDTO extends PersonagemDTO {
    @NotEmpty(message = MSG_PERSONAGEM_ID_NOT_EMPTY)
    @Schema(description = "Código identificador único do personagem", type = "string", required = true)
    private String id;
}
