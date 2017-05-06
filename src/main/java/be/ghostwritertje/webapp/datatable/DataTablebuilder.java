package be.ghostwritertje.webapp.datatable;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 29-Dec-16.
 */
public class DataTablebuilder<T extends Serializable, S> {

    private final List<IColumn<T, S>> columns = new ArrayList<>();

    public DataTablebuilder<T, S> addColumn(IColumn<T, S> column) {
        this.columns.add(column);
        return this;
    }

    public DataTable<T, S> build(String id, IModel<List<T>> data) {
        MySortableDataProvider<T, S> dataProvider = new MySortableDataProvider<>(data);
        BootstrapDefaultDataTable<T, S> components = new BootstrapDefaultDataTable<>(id, this.columns, dataProvider, 25);
        components.striped();
        return components;
    }

    public void attach(WebMarkupContainer parent, String id, IModel<List<T>> data) {
        parent.add(this.build(id, data));
    }


    public static class MySortableDataProvider<T extends Serializable, S> extends SortableDataProvider<T, S>  {
        private static final long serialVersionUID = 3440280114446729862L;
        private final IModel<List<T>> data;

        MySortableDataProvider(IModel<List<T>> data) {
            this.data = data;
        }

        @Override
        public Iterator<? extends T> iterator(long first, long count) {
            List<T> list = this.data.getObject().subList((int)first, (int)(first + count));
            return list.listIterator();
        }

        @Override
        public long size() {
            return this.data.getObject().size();
        }

        @Override
        public IModel<T> model(T object) {
            return new Model<>(object);
        }
    }
}
