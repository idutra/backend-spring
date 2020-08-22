package com.idutra.api.model.dto.rest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Schema(type = "object", description = "Representação do request de personagem.")
public class PersonagemDTO {
    @NotEmpty
    @Schema(description = "Nome do personagem", required = true, type = "string", example = "Harry Potter")
    private String name;
    @NotEmpty
    @Schema(description = "Função do personagem", required = true, type = "string", example = "student")
    private String role;
    @NotEmpty
    @Schema(description = "Escola do personagem", required = true, type = "string", example = "Hogwarts School of Witchcraft and Wizardry")
    private String school;
    @NotEmpty
    @Schema(description = "Código identificador da casa que o personagem pertence", required = true, type = "string", example = "5a05e2b252f721a3cf2ea33f")
    private String houseId;
    @NotEmpty
    @Schema(description = "Patronous do personagem", required = true, type = "string", example = "stag")
    private String patronus;
}
