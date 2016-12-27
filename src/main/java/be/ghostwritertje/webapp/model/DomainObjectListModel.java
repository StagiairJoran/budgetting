package be.ghostwritertje.webapp.model;

import be.ghostwritertje.domain.DomainObject;
import be.ghostwritertje.services.DomainObjectCrudService;
import org.apache.wicket.lambda.WicketFunction;

import java.util.List;
import java.util.Optional;

/**
 * Created by Jorandeboever
 * Date: 27-Dec-16.
 */
public class DomainObjectListModel<T extends DomainObject> extends LoadableListModel<T> {

    private final WicketFunction<DomainObjectCrudService<T>, List<T>> loadFunction;
    private final DomainObjectCrudService<T> service;

    public DomainObjectListModel(DomainObjectCrudService<T> service) {
        this(service, null);
    }

    public DomainObjectListModel(DomainObjectCrudService<T> service, WicketFunction<DomainObjectCrudService<T>, List<T>> loadFunction) {
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
