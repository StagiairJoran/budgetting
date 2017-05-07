package be.ghostwritertje.webapp.datatable;

import be.ghostwritertje.webapp.datatable.CheckColumnBuilder.CheckColumn;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroupSelector;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.danekja.java.util.function.serializable.SerializableFunction;

/**
 * Created by Jorandeboever
 * Date: 01-May-17.
 */
public class CheckColumnBuilder<T, S> extends ColumnBuilderSupport<T, S, CheckColumn<T,S>> {
    @Override
    public CheckColumn<T, S> build(IModel<String> headerModel) {
        return new CheckColumn<>(headerModel);
    }

    public static class CheckColumn<T, S> extends AbstractColumn<T, S> {
        private static final long serialVersionUID = 3713609125399689196L;

        CheckColumn(IModel<String> displayModel) {
            super(displayModel);
        }

        @Override
        public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel) {
            cellItem.add(new CheckPanel<T>(componentId, rowModel));
        }

        @Override
        public Component getHeader(String componentId) {
            return new HeaderPanel(componentId);
        }
    }

    private static class HeaderPanel extends Panel {
        private static final long serialVersionUID = -5759494531315242120L;

        HeaderPanel(String id) {
            super(id);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            this.add(new CheckGroupSelector("check"));
        }
    }

    private static class CheckPanel<T> extends GenericPanel<T> {
        private static final long serialVersionUID = 14708662781618962L;

        CheckPanel(String id, IModel<T> checkModel) {
            super(id, checkModel);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            this.add(new Check<T>("check", this.getModel()));
        }
    }
}
