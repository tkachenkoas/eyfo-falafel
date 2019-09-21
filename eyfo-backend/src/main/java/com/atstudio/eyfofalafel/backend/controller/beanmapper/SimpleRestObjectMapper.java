package com.atstudio.eyfofalafel.backend.controller.beanmapper;

import org.modelmapper.ModelMapper;

import static org.springframework.core.GenericTypeResolver.resolveTypeArguments;

public abstract class SimpleRestObjectMapper<E, R> implements RestObjectMapper <E, R> {

    private final Class<E> entityClass;
    private final Class<R> restDtoClass;
    private final ModelMapper mapper = new ModelMapper();

    protected SimpleRestObjectMapper() {
        Class[] generics = resolveTypeArguments(getClass(), SimpleRestObjectMapper.class);
        entityClass = generics[0];
        restDtoClass = generics[1];
    }

    private Class<E> getEntityClass() {
        return entityClass;
    };
    private Class<R> getRestDtoClass() {
        return restDtoClass;
    };

    protected final <T> T map(Object source, Class<T> targetClass) {
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
