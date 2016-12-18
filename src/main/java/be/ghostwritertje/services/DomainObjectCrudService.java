package be.ghostwritertje.services;

import be.ghostwritertje.domain.DomainObject;

import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
public interface DomainObjectCrudService<T extends DomainObject> {
    T findOne(Integer id);

    void delete(T object);

    T save(T object);

    List<T> findAll();
}
