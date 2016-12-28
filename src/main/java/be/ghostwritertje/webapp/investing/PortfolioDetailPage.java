package be.ghostwritertje.webapp.investing;

import be.ghostwritertje.domain.investing.Allocation;
import be.ghostwritertje.domain.investing.FinancialInstrument;
import be.ghostwritertje.domain.investing.Portfolio;
import be.ghostwritertje.services.investing.FinancialInstrumentService;
import be.ghostwritertje.services.investing.PortfolioService;
import be.ghostwritertje.utilities.Pair;
import be.ghostwritertje.webapp.BasePage;
import be.ghostwritertje.webapp.CustomSession;
import be.ghostwritertje.webapp.charts.ChartBuilderFactory;
import be.ghostwritertje.webapp.form.BaseForm;
import be.ghostwritertje.webapp.form.FormComponentBuilderFactory;
import be.ghostwritertje.webapp.link.LinkBuilderFactory;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Jorandeboever
 * Date: 28-Dec-16.
 */
public class PortfolioDetailPage extends BasePage<Portfolio> {

    @SpringBean
    private FinancialInstrumentService financialInstrumentService;

    @SpringBean
    private PortfolioService portfolioService;

    public PortfolioDetailPage() {
        super(new Model<>(new Portfolio()));
    }

    public PortfolioDetailPage(IModel<Portfolio> model) {
        super(model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        BaseForm<Portfolio> form = new BaseForm<Portfolio>("form", this.getModel());

        FormComponentBuilderFactory.textField()
                .usingDefaults()
                .body(new ResourceModel("name"))
                .attach(form, "name", new LambdaModel<>(
                        () -> this.getModelObject().getName(),
                        s -> this.getModelObject().setName(s)));

        form.add(new ListView<Allocation>("allocations", new LambdaModel<List<Allocation>>(() -> form.getModelObject().getAllocationList(), allocations -> {
        })) {
            @Override
            protected void populateItem(ListItem<Allocation> item) {
                item.add(new Label("quote", item.getModelObject().getFinancialInstrument().getQuote()));
                item.add(new Label("allocation", item.getModelObject().getAllocation()));

            }
        });

        BaseForm<CustomAllocation> allocationBaseForm = new BaseForm<CustomAllocation>("allocationForm", new Model<CustomAllocation>(new CustomAllocation()));

        FormComponentBuilderFactory.textField()
                .usingDefaults()
                .body(new ResourceModel("quote"))
                .attach(allocationBaseForm, "quote", new LambdaModel<String>(() ->
                        allocationBaseForm.getModelObject().getQuote(),
                        s -> allocationBaseForm.getModelObject().setQuote(s)));

        FormComponentBuilderFactory.number()
                .usingDefaults()
                .body(new ResourceModel("allocation"))
                .attach(allocationBaseForm, "allocation", new LambdaModel<Double>(
                        () -> allocationBaseForm.getModelObject().getAllocation(),
                        s -> allocationBaseForm.getModelObject().setAllocation(s)));

        LinkBuilderFactory.submitLink()
                .usingDefaults()
                .attach(allocationBaseForm, "save", (target, components) -> {
                    CustomAllocation customAllocation = allocationBaseForm.getModelObject();
                    FinancialInstrument financialInstrument = this.financialInstrumentService.findByQuote(customAllocation.getQuote());
                    Allocation allocation = new Allocation(financialInstrument, customAllocation.getAllocation());
                    form.getModelObject().getAllocationList().add(allocation);
                    allocationBaseForm.setModelObject(new CustomAllocation());
                    allocationBaseForm.getFormModeModel().setObject(BaseForm.FormMode.EDIT);
                    target.add(this);
                });

        form.add(allocationBaseForm);

        LinkBuilderFactory.submitLink()
                .usingDefaults()
                .attach(form, "save", (target, components) -> {
                    Portfolio portfolio = form.getModelObject();
                    portfolio.setPerson(CustomSession.get().getLoggedInPerson());
                    this.portfolioService.save(portfolio);
                });

        this.add(form);

        Map<String, List<Pair<LocalDate, Double>>> coordinatesMap10 = this.getModelObject().getAllocationList()
                .stream()
                .map(Allocation::getFinancialInstrument)
                .collect(Collectors.toMap(FinancialInstrument::getQuote, (financialInstrument) -> financialInstrument.getValuesFromStartDate(LocalDate.now().minusYears(5))));

        ChartBuilderFactory.splineChart()
                .usingDefaults()
                .title("1 year return")
                .addLine(this.getModelObject().getName(), this.getModelObject().getValuesFromStartDate(LocalDate.now().minusYears(5)), Pair::getK, Pair::getV, 2)
                .addLines(coordinatesMap10, Pair::getK, Pair::getV, 2)
                .attach(this, "chart1");
    }

    private static class CustomAllocation implements Serializable {
        private String quote;
        private Double allocation;

        CustomAllocation() {
        }

        public String getQuote() {
            if (this.quote == null) {
                this.quote = "";
            }
            return quote;
        }

        public void setQuote(String quote) {
            this.quote = quote;
        }

        public Double getAllocation() {
            if (this.allocation == null) {
                this.allocation = 0.0;
            }
            return allocation;
        }

        public void setAllocation(Double allocation) {
            this.allocation = allocation;
        }
    }
}
