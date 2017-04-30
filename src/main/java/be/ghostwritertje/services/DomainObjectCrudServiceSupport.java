package be.ghostwritertje.services;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */

import be.ghostwritertje.domain.DomainObject;
import com.google.common.collect.Lists;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public abstract class DomainObjectCrudServiceSupport<T extends DomainObject> implements DomainObjectCrudService<T> {

    protected abstract CrudRepository<T, String> getDao();

    @Override
    public T findOne(String id) {
        return this.getDao().findOne(id);
    }

    @Override
    public List<T> findAll() {
        return Lists.newArrayList(this.getDao().findAll());
    }

    @Override
    public void delete(T object) {
        this.getDao().delete(object);
    }

    @Override
    public T save(T object) {
        return this.getDao().save(object);
    }

    @Override
    public Iterable<T> save(Iterable<T> objects) {
        return this.getDao().save(objects);
    }
}
