package com.idutra.api.model.dto.rest.response;

import com.idutra.api.model.dto.rest.RestDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseErroDTO implements RestDTO {
    private static final long serialVersionUID = 1451435524967414929L;

    private String mensagemErro;
    private String detalheErro;
}
