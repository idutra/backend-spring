package com.idutra.api.model.dto.rest.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

import static com.idutra.api.constants.MensagemConstant.MSG_PERSONAGEM_ID_NOT_EMPTY;

@Data
@Schema(type = "object", description = "Representação do request de alterar.")
public class AlterarPersonagemRequestDTO {
    @NotEmpty(message = MSG_PERSONAGEM_ID_NOT_EMPTY)
    @Schema(description = "Código identificador único do personagem", type = "string", required = true)
    private String id;
    @Schema(description = "Função do personagem", type = "string", example = "student")
    private String role;
    @Schema(description = "Escola do personagem", type = "string", example = "Hogwarts School of Witchcraft and Wizardry")
    private String school;
    @Schema(description = "Patronous do personagem", type = "string", example = "stag")
    private String patronus;
}
