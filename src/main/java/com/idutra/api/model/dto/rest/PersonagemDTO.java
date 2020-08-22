package com.idutra.api.model.dto.rest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

import static com.idutra.api.constants.MensagemConstant.MSG_PERSONAGEM_HOUSE_ID_NOT_EMPTY;
import static com.idutra.api.constants.MensagemConstant.MSG_PERSONAGEM_ID_NOT_EMPTY;
import static com.idutra.api.constants.MensagemConstant.MSG_PERSONAGEM_PATRONUS_NOT_EMPTY;
import static com.idutra.api.constants.MensagemConstant.MSG_PERSONAGEM_ROLE_NOT_EMPTY;
import static com.idutra.api.constants.MensagemConstant.MSG_PERSONAGEM_SCHOOL_NOT_EMPTY;

@Data
@Schema(type = "object", description = "Representação do request de personagem.")
public class PersonagemDTO {
    @NotEmpty(message = MSG_PERSONAGEM_ID_NOT_EMPTY)
    @Schema(description = "Nome do personagem", required = true, type = "string", example = "Harry Potter")
    private String name;
    @NotEmpty(message = MSG_PERSONAGEM_ROLE_NOT_EMPTY)
    @Schema(description = "Função do personagem", required = true, type = "string", example = "student")
    private String role;
    @NotEmpty(message = MSG_PERSONAGEM_SCHOOL_NOT_EMPTY)
    @Schema(description = "Escola do personagem", required = true, type = "string", example = "Hogwarts School of Witchcraft and Wizardry")
    private String school;
    @NotEmpty(message = MSG_PERSONAGEM_HOUSE_ID_NOT_EMPTY)
    @Schema(description = "Código identificador da casa que o personagem pertence", required = true, type = "string", example = "5a05e2b252f721a3cf2ea33f")
    private String houseId;
    @NotEmpty(message = MSG_PERSONAGEM_PATRONUS_NOT_EMPTY)
    @Schema(description = "Patronous do personagem", required = true, type = "string", example = "stag")
    private String patronus;
}
