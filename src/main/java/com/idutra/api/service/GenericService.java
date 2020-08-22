package com.idutra.api.service;

import com.idutra.api.repository.GenericRepository;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Log4j2
public abstract class GenericService<R extends GenericRepository<T, I>, T, I> {
    R repository;

    public GenericService(R repository) {
        this.repository = repository;
    }

    public T processarMensagem(@Valid @NotNull(message = "genericService.objeto.notnull.message") final T entity) {
        return this.save(entity);
    }

    @Transactional
    public T save(@Valid @NotNull(message = "genericService.objeto.notnull.message") final T entity) {
        return repository.save(entity);
    }

    public Optional<T> findById(@NotNull(message = "genericService.id.notnull.message") I id) {
        return repository.findById(id);
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
