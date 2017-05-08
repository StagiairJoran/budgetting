package be.ghostwritertje.webapp.model;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.util.ListModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 27-Dec-16.
 */
public abstract class LoadableListModel<T extends Serializable> extends ListModel<T> {

    private static final long serialVersionUID = -6197721750882580280L;

    protected LoadableListModel() {
        Injector.get().inject(this);
    }

    @Override
    public List<T> getObject() {
        if (super.getObject() == null) {
            super.setObject(this.load());
        }
        return super.getObject();
    }

    protected abstract List<T> load();
}
