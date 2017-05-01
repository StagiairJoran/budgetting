package be.ghostwritertje.webapp.budgetting;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.services.budgetting.BankAccountsHistoricData;
import be.ghostwritertje.services.budgetting.StatementService;
import be.ghostwritertje.webapp.charts.ChartBuilderFactory;
import be.ghostwritertje.webapp.model.LoadableModel;
import com.google.common.collect.ImmutableMap;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Jorandeboever
 * Date: 01-May-17.
 */
public class BankAccountsGraph extends GenericPanel<Person> {
    private static final long serialVersionUID = 1178736441274538229L;

    private final IModel<BankAccountsHistoricData> bankAccountsHistoricDataIModel;

    public BankAccountsGraph(String id, IModel<Person> model) {
        super(id, model);
        this.bankAccountsHistoricDataIModel = new BankAccountsHistoricDataModel(model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        Map<LocalDate, BigDecimal> dataSeries1 = ImmutableMap.of(
                LocalDate.of(2016,1,1), new BigDecimal(100),
                LocalDate.of(2016,2,1), new BigDecimal(200),
                LocalDate.of(2016,3,1), new BigDecimal(300)
        );

        Map<LocalDate, BigDecimal> dataSeries2 = ImmutableMap.of(
                LocalDate.of(2016,1,1), new BigDecimal(250),
                LocalDate.of(2016,2,1), new BigDecimal(50),
                LocalDate.of(2016,3,1), new BigDecimal(300)
        );

        ChartBuilderFactory.area()
                .setxAxis(this.bankAccountsHistoricDataIModel.getObject().getDate())
                .addSeries(this.bankAccountsHistoricDataIModel.getObject().getValuesMap().entrySet().stream().collect(Collectors.toMap(o -> o.getKey().toString(), Map.Entry::getValue)))
                .attach(this, "chart");

    }

    private static class BankAccountsHistoricDataModel extends LoadableModel<BankAccountsHistoricData> {

        @SpringBean
        private StatementService statementService;

        private final IModel<Person> personIModel;

        private BankAccountsHistoricDataModel(IModel<Person> personIModel) {
            this.personIModel = personIModel;
        }

        @Override
        protected BankAccountsHistoricData load() {
            return this.statementService.getBankAccountsHistoricDataForPersonByMonth(this.personIModel.getObject());
        }
    }
}
