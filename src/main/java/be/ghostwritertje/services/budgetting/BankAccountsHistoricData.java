package be.ghostwritertje.services.budgetting;

import be.ghostwritertje.domain.budgetting.BankAccount;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class BankAccountsHistoricData implements Serializable {
    private static final long serialVersionUID = 2036282054799161039L;

    private final List<String> dateStrings;
    private final Map<BankAccount, List<Number>> valuesMap;

    public BankAccountsHistoricData(List<String> dateStrings, Map<BankAccount, List<Number>> valuesMap) {
        this.dateStrings = dateStrings;
        this.valuesMap = valuesMap;
    }

    public List<String> getDate() {
        return this.dateStrings;
    }

    public Map<BankAccount, List<Number>> getValuesMap() {
        return this.valuesMap;
    }
}