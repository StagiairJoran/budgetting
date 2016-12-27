package be.ghostwritertje.webapp.investing;

import be.ghostwritertje.domain.investing.FinancialInstrument;
import be.ghostwritertje.services.investing.FinancialInstrumentService;
import be.ghostwritertje.webapp.model.LoadableListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 27-Dec-16.
 */
public class FinancialInstrumentListModel extends LoadableListModel<FinancialInstrument> {

    @SpringBean
    private FinancialInstrumentService financialInstrumentService;

    @Override
    protected List<FinancialInstrument> load() {
        return this.financialInstrumentService.findAll();
    }
}
