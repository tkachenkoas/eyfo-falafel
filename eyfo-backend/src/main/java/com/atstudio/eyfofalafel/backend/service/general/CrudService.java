package com.atstudio.eyfofalafel.backend.service.general;

import com.google.common.collect.Lists;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Service
@SuppressWarnings("unchecked")
public interface CrudService<T, ID extends Serializable> {

    CrudRepository<T, ID> getCrudRepository();

    default List<T> findAll() {
        return Lists.newArrayList(getCrudRepository().findAll());
    }

    default T save(T entity) {
        return getCrudRepository().save(entity);
    }

    default Optional<T> findById(ID id) {
        return getCrudRepository().findById(id);
    }

}
