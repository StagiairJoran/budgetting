package be.ghostwritertje.services.car;

import be.ghostwritertje.domain.car.Refueling;

/**
 * Created by Jorandeboever
 * Date: 20-Dec-16.
 */
public class RefuelingSearchResult {
    private Refueling refueling;
    private Double verbruik;

    public RefuelingSearchResult(Refueling refueling) {
        this.refueling = refueling;
    }

    public Refueling getRefueling() {
        return refueling;
    }

    public Double getVerbruik() {
        return verbruik;
    }

    public RefuelingSearchResult setVerbruik(Double verbruik) {
        this.verbruik = verbruik;
        return this;
    }
}
