package com.idutra.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idutra.api.AbstractContextTest;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

public abstract class AbstractControllerTest extends AbstractContextTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    @Getter
    protected ModelMapper modelMapper;


    public String toJson(Object objeto) {
        try {
            return new ObjectMapper().writeValueAsString(objeto);
        } catch (JsonProcessingException e) {
            return null;
        }
    }


}
