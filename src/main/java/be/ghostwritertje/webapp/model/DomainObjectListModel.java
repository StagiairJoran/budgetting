package be.ghostwritertje.webapp.model;

import be.ghostwritertje.domain.DomainObject;
import be.ghostwritertje.services.DomainObjectCrudService;
import org.danekja.java.util.function.serializable.SerializableFunction;

import java.util.List;
import java.util.Optional;

/**
 * Created by Jorandeboever
 * Date: 27-Dec-16.
 */
public class DomainObjectListModel<T extends DomainObject, S extends DomainObjectCrudService<T>> extends LoadableListModel<T> {

    private final SerializableFunction<S, List<T>> loadFunction;
    private final S service;

    public DomainObjectListModel(S service) {
        this(service, null);
    }

    public DomainObjectListModel(S service, SerializableFunction<S, List<T>> loadFunction) {
        this.service = service;
        this.loadFunction = loadFunction;
    }

    @Override
    protected List<T> load() {
        return Optional.ofNullable(this.loadFunction)
                .map(f -> f.apply(this.service))
                .orElse(this.service.findAll());
    }
}
