package com.idutra.api.service.hpapi.model;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Data
@JsonRootName("error")
public class ErroDTO {
    private String error;
}
