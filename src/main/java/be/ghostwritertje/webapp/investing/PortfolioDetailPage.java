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
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.Serializable;
import java.math.BigDecimal;
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
                .attach(form, "name", LambdaModel.of(this.getModel(),
                      Portfolio::getName, Portfolio::setName));

        form.add(new ListView<Allocation>("allocations", LambdaModel.of(form.getModel(), Portfolio::getAllocationList)) {
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
                .attach(allocationBaseForm, "quote", LambdaModel.of(allocationBaseForm.getModel(), CustomAllocation::getQuote, CustomAllocation::setQuote));

        FormComponentBuilderFactory.number(BigDecimal.class)
                .usingDefaults()
                .body(new ResourceModel("allocation"))
                .attach(allocationBaseForm, "allocation", LambdaModel.of(allocationBaseForm.getModel(), CustomAllocation::getAllocation, CustomAllocation::setAllocation));

        LinkBuilderFactory.submitLink((target, components) -> {
            CustomAllocation customAllocation = allocationBaseForm.getModelObject();
            FinancialInstrument financialInstrument = this.financialInstrumentService.findByQuote(customAllocation.getQuote());
            Allocation allocation = new Allocation(financialInstrument, customAllocation.getAllocation());
            form.getModelObject().getAllocationList().add(allocation);
            allocationBaseForm.setModelObject(new CustomAllocation());
            allocationBaseForm.getFormModeModel().setObject(BaseForm.FormMode.EDIT);
            target.add(this);
        })
                .usingDefaults()
                .attach(allocationBaseForm, "save");

        form.add(allocationBaseForm);

        LinkBuilderFactory.submitLink((target, components) -> {
            Portfolio portfolio = form.getModelObject();
            portfolio.setPerson(CustomSession.get().getLoggedInPerson());
            this.portfolioService.save(portfolio);
        })
                .usingDefaults()
                .attach(form, "save");

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
        private BigDecimal allocation;

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

        public BigDecimal getAllocation() {
            if (this.allocation == null) {
                this.allocation = BigDecimal.ZERO;
            }
            return allocation;
        }

        public void setAllocation(BigDecimal allocation) {
            this.allocation = allocation;
        }
    }
}
