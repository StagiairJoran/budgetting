package be.ghostwritertje.webapp.datatable;

import java.io.Serializable;

/**
 * Created by Jorandeboever
 * Date: 29-Dec-16.
 */
public class DataTableBuilderFactory {
    public static <T extends Serializable, S> DataTablebuilder<T, S> simple() {
        return new DataTablebuilder<>();
    }
}
