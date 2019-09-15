package com.atstudio.eyfofalafel.backend.controller.beanmapper;

import org.modelmapper.ModelMapper;
import org.springframework.core.GenericTypeResolver;

public abstract class SimpleRestObjectMapper<E, R> implements RestObjectMapper <E, R> {

    private final Class<E> entityClass;
    private final Class<R> restDtoClass;

    protected SimpleRestObjectMapper() {
        Class[] generics = GenericTypeResolver.resolveTypeArguments(getClass(), SimpleRestObjectMapper.class);
        entityClass = generics[0];
        restDtoClass = generics[1];
    }

    protected Class<E> getEntityClass() {
        return entityClass;
    };
    protected Class<R> getRestDtoClass() {
        return restDtoClass;
    };

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
