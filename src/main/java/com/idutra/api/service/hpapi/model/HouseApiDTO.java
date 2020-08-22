package com.idutra.api.service.hpapi.model;

import lombok.Data;

@Data
public class HouseApiDTO {
    private String _id;
    private String name;
    private String mascot;
    private String headOfHouse;
    private String houseGhost;
    private String founder;
    private String __v;
    private String school;
    private Object[] members;
    private Object[] values;
    private Object[] colors;
}
