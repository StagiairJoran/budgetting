package be.ghostwritertje.webapp.datatable;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.checkbox.bootstrapcheckbox.BootstrapCheckBoxPicker;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.lambda.WicketFunction;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;

public class CheckBoxColumn<T, S> extends AbstractColumn<T, S> {
    private final WicketFunction<T, Boolean> booleanFunction;

    public CheckBoxColumn(IModel<String> displayModel, WicketFunction<T, Boolean> booleanFunction) {
        super(displayModel);
        this.booleanFunction = booleanFunction;
    }

    public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel) {
        cellItem.add(new CheckPanel(componentId, new LambdaModel<Boolean>(() -> booleanFunction.apply(rowModel.getObject()), aBoolean -> {
        })));
    }

    private CheckBox newCheckBox(String id, IModel<Boolean> checkModel) {
        BootstrapCheckBoxPicker check = new BootstrapCheckBoxPicker(id, checkModel);
        check.setEnabled(false);
        return check;
    }


    private class CheckPanel extends Panel {
        public CheckPanel(String id, IModel<Boolean> checkModel) {
            super(id);
            this.add(newCheckBox("check", checkModel));
        }
    }
}