package be.ghostwritertje.webapp.form;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IFormSubmitter;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.io.Serializable;

/**
 * Created by Jorandeboever
 * Date: 23-Dec-16.
 */
public class BaseForm<T extends Serializable> extends Form<T> {

    private final IModel<FormMode> formModeModel;

    public BaseForm(String id, IModel<T> model) {
        super(id, model);
        this.formModeModel = new Model<>(FormMode.EDIT);
    }

    public IModel<FormMode> getFormModeModel() {
        return formModeModel;
    }

    @Override
    public void process(IFormSubmitter submittingComponent) {
        super.process(submittingComponent);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        this.setOutputMarkupId(true);
    }

    public enum  FormMode {
        READ, EDIT
    }
}
