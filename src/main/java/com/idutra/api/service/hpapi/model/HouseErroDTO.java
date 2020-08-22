package com.idutra.api.service.hpapi.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class HouseErroDTO {

    String message;
    String name;
    String stringValue;
    String kind;
    String value;
    String path;


    public String toJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return this.toString();
        }
    }
}
