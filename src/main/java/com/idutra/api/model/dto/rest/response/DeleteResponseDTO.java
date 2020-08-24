package com.idutra.api.model.dto.rest.response;

import com.idutra.api.model.dto.rest.RestDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Objeto que representa o resultado da solicitação de exclusão de um personagem", type = "object")
public class DeleteResponseDTO implements RestDTO {
    @Schema(description = "Código identificador do personagem excluído",type = "string")
    private String id;
    @Schema(description = "Mensagem de retorno da solicitação")
    private String mensagem;
}
