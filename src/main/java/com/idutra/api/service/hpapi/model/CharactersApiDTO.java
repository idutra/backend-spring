package com.idutra.api.service.hpapi.model;

import lombok.Data;

@Data
public class CharactersApiDTO {
    private String _id;
    private String __v;
    private String name;
    private String role;
    private String house;
    private String school;
    private Boolean ministryOfMagic;
    private Boolean orderOfThePhoenix;
    private Boolean dumbledoresArmy;
    private Boolean deathEater;
    private String bloodStatus;
    private String species;
    private String boggart;
    private String alias;
    private String wand;
    private String animagus;
    private String patronus;
}
