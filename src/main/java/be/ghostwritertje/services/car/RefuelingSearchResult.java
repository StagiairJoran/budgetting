package be.ghostwritertje.services.car;

import be.ghostwritertje.domain.car.Refueling;

import java.math.BigDecimal;

/**
 * Created by Jorandeboever
 * Date: 20-Dec-16.
 */
public class RefuelingSearchResult {
    private Refueling refueling;
    private BigDecimal consumption;
    private BigDecimal kilometresPerMonth;
    private long numberOfDays;
    private BigDecimal totalDistanceDriven;

    public RefuelingSearchResult(Refueling refueling) {
        this.refueling = refueling;
    }

    public Refueling getRefueling() {
        return this.refueling;
    }

    public BigDecimal getConsumption() {
        return this.consumption;
    }

    public BigDecimal getKilometresPerMonth() {
        return this.kilometresPerMonth;
    }

    public long getNumberOfDays() {
        return this.numberOfDays;
    }

    public BigDecimal getTotalDistanceDriven() {
        return this.totalDistanceDriven;
    }

    public RefuelingSearchResult setNumberOfDays(long numberOfDays) {
        this.numberOfDays = numberOfDays;
        return this;
    }

    public RefuelingSearchResult setTotalDistanceDriven(BigDecimal totalDistanceDriven) {
        this.totalDistanceDriven = totalDistanceDriven;
        return this;
    }

    public RefuelingSearchResult setKilometresPerMonth(BigDecimal kilometresPerMonth) {
        this.kilometresPerMonth = kilometresPerMonth;
        return this;
    }

    public RefuelingSearchResult setConsumption(BigDecimal consumption) {
        this.consumption = consumption;
        return this;
    }
}
