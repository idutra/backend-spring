package com.idutra.api.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.google.common.collect.Lists;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;

public class ApiClientFactory {
    public static <T> T createApiClient(String apiBaseAddress, Class<T> clazz) {
        JsonMapper jsonMapper = (JsonMapper) new JsonMapper()
                .registerModule(new JaxbAnnotationModule())
                .registerModule(new JavaTimeModule())
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .enable(SerializationFeature.INDENT_OUTPUT);

        JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
        provider.setMapper(jsonMapper);
        return JAXRSClientFactory.create(apiBaseAddress, clazz, Lists.newArrayList(provider));
    }
}
