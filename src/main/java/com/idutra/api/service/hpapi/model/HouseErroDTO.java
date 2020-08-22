package com.idutra.api.service.hpapi.model;

import lombok.Data;

@Data
public class HouseErroDTO {
    String message;
    String name;
    String stringValue;
    String kind;
    String value;
    String path;
}
