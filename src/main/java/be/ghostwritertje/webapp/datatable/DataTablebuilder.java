package be.ghostwritertje.webapp.datatable;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
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
        MySortableDataProvicer<T, S> dataProvicer = new MySortableDataProvicer<>(data);

        return new BootstrapDefaultDataTable<>(id, columns, dataProvicer, 20);
    }

    public static class MySortableDataProvicer<T extends Serializable, S> extends SortableDataProvider<T, S> {
        private final IModel<List<T>> data;
//        private WicketFunction<T, Boolean> filterPredicate = t -> true;

        MySortableDataProvicer(IModel<List<T>> data) {
            this.data = data;
        }

        @Override
        public Iterator<? extends T> iterator(long first, long count) {
            List<T> list = data.getObject();
            return list.listIterator();
        }

        @Override
        public long size() {
            return data.getObject().size();
        }

        @Override
        public IModel<T> model(T object) {
            return new Model<>(object);
        }

//        private List<T> getFilteredData(){
//            return this.data.getObject().stream()
//                    .filter(this.filterPredicate::apply)
//                    .collect(Collectors.toList());
//        }
//
//        public void setFilterPredicate(WicketFunction<T, Boolean> filterPredicate) {
//            this.filterPredicate = filterPredicate;
//        }
    }

//    @FunctionalInterface
//    public interface SerializableComparator<T> extends Comparator<T>, Serializable {
//
//        public static <T, U extends Comparable<? super U>> SerializableComparator<T> comparing(Function<? super T, ? extends U> keyExtractor){
//            return (c1, c2) -> keyExtractor.apply(c1).compareTo(keyExtractor.apply(c2));
//        }
//
//        public static <T, U> SerializableComparator<T> comparing(Function<? super T, ? extends U> keyExtractor, Comparator<? super U> keyComparator){
//            Objects.requireNonNull(keyExtractor);
//            Objects.requireNonNull(keyComparator);
//            return (c1, c2) -> keyComparator.compare(keyExtractor.apply(c1), keyExtractor.apply(c2));
//        }
//    }
}
