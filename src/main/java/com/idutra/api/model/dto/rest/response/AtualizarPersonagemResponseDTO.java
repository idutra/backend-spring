package com.idutra.api.model.dto.rest.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(type = "object", description = "Representação do response de atualizar personagem.")
public class AtualizarPersonagemResponseDTO extends PersonagemResponseDTO {
}
