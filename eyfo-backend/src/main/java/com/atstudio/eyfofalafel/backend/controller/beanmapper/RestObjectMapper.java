package com.atstudio.eyfofalafel.backend.controller.beanmapper;

// TODO refactor all the mappers to ModelMapper converters
public interface RestObjectMapper<E, R> {

    E toEntity(R restObject);
    R toRest(E entity);
    default R toShortRest(E entity) {
        return toRest(entity);
    }

}
