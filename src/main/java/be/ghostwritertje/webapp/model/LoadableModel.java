package be.ghostwritertje.webapp.model;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.Model;

import java.io.Serializable;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
public abstract class LoadableModel<T extends Serializable> extends Model<T> {
    private static final long serialVersionUID = -6197721750882580280L;

    protected LoadableModel() {
        Injector.get().inject(this);
    }

    protected LoadableModel(T object) {
        super(object);
        Injector.get().inject(this);
    }

    @Override
    public T getObject() {
        if (super.getObject() == null) {
            this.setObject(this.load());
        }
        return super.getObject();
    }

    protected abstract T load();
}
