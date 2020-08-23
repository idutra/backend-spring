package com.idutra.api.model.dto.rest.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ListarPersonagemRequestDTO {
    @Schema(description = "Id do personagem", type = "string")
    private String id;
    @Schema(description = "Nome do personagem", type = "string")
    private String name;
    @Schema(description = "Função do personagem", type = "string")
    private String role;
    @Schema(description = "Escola do personagem", type = "string")
    private String school;
    @Schema(description = "Código identificador da casa que o personagem pertence", type = "string")
    private String houseId;
    @Schema(description = "Patronous do personagem", type = "string")
    private String patronus;
}
