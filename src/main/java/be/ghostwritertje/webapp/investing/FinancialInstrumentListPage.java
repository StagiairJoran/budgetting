package be.ghostwritertje.webapp.investing;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.investing.FinancialInstrument;
import be.ghostwritertje.services.investing.FinancialInstrumentService;
import be.ghostwritertje.utilities.Pair;
import be.ghostwritertje.webapp.BasePage;
import be.ghostwritertje.webapp.charts.ChartBuilderFactory;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        LinkBuilderFactory.submitLink( save(form.getModel()))
                .usingDefaults()
                .attach(form, "save");


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
        Map<String,List<Pair<LocalDate, Double>>> coordinatesMap1 = this.financialInstrumentListModel.getObject()
                .stream()
                .collect(Collectors.toMap(FinancialInstrument::getQuote, (financialInstrument) -> financialInstrument.getValuesFromStartDate(LocalDate.now().minusYears(1))));

        Map<String,List<Pair<LocalDate, Double>>> coordinatesMap3 = this.financialInstrumentListModel.getObject()
                .stream()
                .collect(Collectors.toMap(FinancialInstrument::getQuote, (financialInstrument) -> financialInstrument.getValuesFromStartDate(LocalDate.now().minusYears(3))));

        Map<String,List<Pair<LocalDate, Double>>> coordinatesMap5 = this.financialInstrumentListModel.getObject()
                .stream()
                .collect(Collectors.toMap(FinancialInstrument::getQuote, (financialInstrument) -> financialInstrument.getValuesFromStartDate(LocalDate.now().minusYears(5))));

        Map<String,List<Pair<LocalDate, Double>>> coordinatesMap7 = this.financialInstrumentListModel.getObject()
                .stream()
                .collect(Collectors.toMap(FinancialInstrument::getQuote, (financialInstrument) -> financialInstrument.getValuesFromStartDate(LocalDate.now().minusYears(7))));

        Map<String,List<Pair<LocalDate, Double>>> coordinatesMap10 = this.financialInstrumentListModel.getObject()
                .stream()
                .collect(Collectors.toMap(FinancialInstrument::getQuote, (financialInstrument) -> financialInstrument.getValuesFromStartDate(LocalDate.now().minusYears(10))));


        ChartBuilderFactory.splineChart()
                .usingDefaults()
                .title("1 year return")
                .addLines(coordinatesMap3, Pair::getK, Pair::getV, 2)
                .attach(this, "chart1");


        ChartBuilderFactory.splineChart()
                .usingDefaults()
                .title("3 year return")
                .addLines(coordinatesMap3, Pair::getK, Pair::getV, 2)
                .attach(this, "chart3");

        ChartBuilderFactory.splineChart()
                .usingDefaults()
                .title("5 year return")
                .addLines(coordinatesMap5, Pair::getK, Pair::getV, 2)
                .attach(this, "chart5");

        ChartBuilderFactory.splineChart()
                .usingDefaults()
                .title("7 year return")
                .addLines(coordinatesMap7, Pair::getK, Pair::getV, 2)
                .attach(this, "chart7");


        ChartBuilderFactory.splineChart()
                .usingDefaults()
                .title("10 year return")
                .addLines(coordinatesMap10, Pair::getK, Pair::getV, 2)
                .attach(this, "chart10");

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
