package be.ghostwritertje.services.car;

import be.ghostwritertje.domain.car.Refueling;

/**
 * Created by Jorandeboever
 * Date: 20-Dec-16.
 */
public class RefuelingSearchResult {
    private Refueling refueling;
    private Double consumption;
    private Double kilometresPerMonth;

    public RefuelingSearchResult(Refueling refueling) {
        this.refueling = refueling;
    }

    public Refueling getRefueling() {
        return refueling;
    }

    public Double getConsumption() {
        return consumption;
    }

    public Double getKilometresPerMonth() {
        return kilometresPerMonth;
    }

    public RefuelingSearchResult setKilometresPerMonth(Double kilometresPerMonth) {
        this.kilometresPerMonth = kilometresPerMonth;
        return this;
    }

    public RefuelingSearchResult setConsumption(Double consumption) {
        this.consumption = consumption;
        return this;
    }
}
