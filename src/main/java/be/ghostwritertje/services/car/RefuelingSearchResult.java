package be.ghostwritertje.services.car;

import be.ghostwritertje.domain.car.Refueling;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Jorandeboever
 * Date: 20-Dec-16.
 */
public class RefuelingSearchResult implements Serializable {
    private static final long serialVersionUID = -1066394414299462890L;

    private final Refueling refueling;
    private BigDecimal consumption;
    private BigDecimal kilometresPerMonth;
    private BigDecimal kilometresPerYear;
    private long numberOfDays;
    private BigDecimal totalDistanceDriven;

    RefuelingSearchResult(Refueling refueling) {
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

    public BigDecimal getKilometresPerYear() {
        return this.kilometresPerYear;
    }

    public RefuelingSearchResult setKilometresPerYear(BigDecimal kilometresPerYear) {
        this.kilometresPerYear = kilometresPerYear;
        return this;
    }

    long getNumberOfDays() {
        return this.numberOfDays;
    }

    BigDecimal getTotalDistanceDriven() {
        return this.totalDistanceDriven;
    }

    RefuelingSearchResult setNumberOfDays(long numberOfDays) {
        this.numberOfDays = numberOfDays;
        return this;
    }

    RefuelingSearchResult setTotalDistanceDriven(BigDecimal totalDistanceDriven) {
        this.totalDistanceDriven = totalDistanceDriven;
        return this;
    }

    RefuelingSearchResult setKilometresPerMonth(BigDecimal kilometresPerMonth) {
        this.kilometresPerMonth = kilometresPerMonth;
        return this;
    }

    RefuelingSearchResult setConsumption(BigDecimal consumption) {
        this.consumption = consumption;
        return this;
    }
}
