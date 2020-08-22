package com.idutra.api.service;

import com.idutra.api.repository.GenericRepository;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;

@Log4j2
public abstract class GenericService<R extends GenericRepository<T, I>, T, I> {
    R repository;

    public GenericService(R repository) {
        this.repository = repository;
    }

    protected ModelMapper instanceModelMapper(PropertyMap propertyMap) {
        ModelMapper modelMapper = new ModelMapper();
        if (propertyMap != null) {
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            modelMapper.addMappings(propertyMap);
        }
        return modelMapper;
    }


}
