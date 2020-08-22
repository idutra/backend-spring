package com.idutra.api.rest.controller.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Log4j2
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public abstract class AbstractController {
    protected String getUriNormalizada(String... uris) {
        StringBuilder uri = new StringBuilder();
        String before = null;

        for (String current : uris) {
            if (before == null) {
                before = current;
            } else {
                if (before.endsWith("/") && current.startsWith("/")) {
                    current = current.replaceFirst("/", "");
                } else if (!before.endsWith("/") && !current.startsWith("/")) {
                    current = "/".concat(current);
                }
                uri.append(before.replace(" ", ""));
                before = current;
            }
        }
        if (before != null) {
            uri.append(before.replace(" ", ""));
        }

        return uri.toString();
    }

    private String toJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error(e);
            return object.toString();
        }
    }
}
