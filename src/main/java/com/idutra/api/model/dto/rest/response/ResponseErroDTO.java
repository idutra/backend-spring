package com.idutra.api.model.dto.rest.response;

import com.idutra.api.model.dto.rest.RestDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(type = "object", description = "Representação do responde de erros.")
public class ResponseErroDTO implements RestDTO {
    private String mensagemErro;
    private String detalheErro;
}
