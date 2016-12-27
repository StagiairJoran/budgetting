package be.ghostwritertje.webapp.investing;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.investing.FinancialInstrument;
import be.ghostwritertje.services.investing.FinancialInstrumentService;
import be.ghostwritertje.webapp.BasePage;
import be.ghostwritertje.webapp.form.BaseForm;
import be.ghostwritertje.webapp.form.FormComponentBuilderFactory;
import be.ghostwritertje.webapp.link.LinkBuilderFactory;
import be.ghostwritertje.webapp.model.DomainObjectListModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.lambda.WicketBiConsumer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 26-Dec-16.
 */
public class FinancialInstrumentListPage extends BasePage<Person> {

    @SpringBean
    private FinancialInstrumentService financialInstrumentService;
    private final IModel<List<FinancialInstrument>> financialInstrumentListModel;

    public FinancialInstrumentListPage() {
        this.financialInstrumentListModel = new DomainObjectListModel<>(this.financialInstrumentService);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        BaseForm<FinancialInstrument> form = new BaseForm<FinancialInstrument>("form", new Model<>(new FinancialInstrument()));
        FormComponentBuilderFactory.textField()
                .usingDefaults()
                .attach(form, "quote", new LambdaModel<>(() -> form.getModelObject().getQuote(), s -> form.getModelObject().setQuote(s)));

        LinkBuilderFactory.submitLink()
                .usingDefaults()
                .attach(form, "save", save(form.getModel()));


        this.add(form);

        this.add(new ListView<FinancialInstrument>("financialInstruments", this.financialInstrumentListModel) {
            @Override
            protected void onInitialize() {
                super.onInitialize();
                this.setViewSize(25);
            }

            @Override
            protected void populateItem(ListItem<FinancialInstrument> item) {
                item.add(new Label("quote", item.getModelObject().getQuote()));
                item.add(new Label("yearToDateReturn", item.getModelObject().getYearToDateReturn()));
                item.add(new Label("fiveYearReturn", item.getModelObject().get5yearReturn()));
                item.add(new Label("currentPrice", item.getModelObject().getCurrentPrice()));
                item.add(new Link<FinancialInstrument>("link", item.getModel()) {
                    @Override
                    public void onClick() {
                        setResponsePage(new FinancialInstrumentPage(item.getModel()));
                    }
                });
            }
        });
    }

    @SuppressWarnings("unchecked")
    public BaseForm<FinancialInstrument> getForm() {
        return (BaseForm<FinancialInstrument>) this.get("form");
    }

    public TextField getQuote() {
        return (TextField) this.getForm().get("quote");
    }

    private static WicketBiConsumer<AjaxRequestTarget, AjaxSubmitLink> save(IModel<FinancialInstrument> model) {
        return (target, components) -> {
            FinancialInstrumentListPage parent = components.findParent(FinancialInstrumentListPage.class);
            BaseForm<FinancialInstrument> form = parent.getForm();
            TextField textField = parent.getQuote();
            FinancialInstrument result = parent.financialInstrumentService.save(model.getObject());
            if(result == null){
                textField.error("Quote doesn't exist");
            }else {
                model.setObject(new FinancialInstrument());
            }
            parent.financialInstrumentListModel.setObject(null);
            form.getFormModeModel().setObject(BaseForm.FormMode.EDIT);
            target.add(parent);
        };
    }
}
