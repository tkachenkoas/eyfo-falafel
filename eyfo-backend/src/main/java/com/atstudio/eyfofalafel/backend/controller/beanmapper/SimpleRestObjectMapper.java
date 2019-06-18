package com.atstudio.eyfofalafel.backend.controller.beanmapper;

import org.modelmapper.ModelMapper;

public abstract class SimpleRestObjectMapper<E, R> implements RestObjectMapper <E, R> {

    protected abstract Class<E> getEntityClass();
    protected abstract Class<R> getRestDtoClass();

    private ModelMapper mapper = new ModelMapper();

    protected <T> T map(Object source, Class<T> targetClass) {
        return mapper.map(source, targetClass);
    }

    @Override
    public E toEntity(R restObject) {
        return map(restObject, getEntityClass());
    }

    @Override
    public R toRest(E entity) {
        return map(entity, getRestDtoClass());
    }

}
