package be.ghostwritertje.domain.car;

import be.ghostwritertje.domain.Bedrag;
import be.ghostwritertje.domain.Currency;
import be.ghostwritertje.domain.DomainObject;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
@Entity
@Table(name = "T_REFUELING")
public class Refueling extends DomainObject {
    private static final long serialVersionUID = 4221474705174574358L;

    @ManyToOne
    @JoinColumn(name = "car_UUID")
    private Car car;
    private LocalDate date;
    private BigDecimal liters;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(column = @Column(name = "price"), name = "value")
    })
    private Bedrag bedrag;
    private BigDecimal kilometres;
    private BigDecimal pricePerLiter;

    private boolean fuelTankFull = true;

    public Refueling() {
    }

    public Refueling(BigDecimal liters, BigDecimal kilometres, LocalDate date) {
        this.liters = liters;
        this.kilometres = kilometres;
        this.date = date;
    }

    public Refueling(BigDecimal liters, BigDecimal kilometres, LocalDate date, boolean fuelTankFull) {
        this.liters = liters;
        this.kilometres = kilometres;
        this.date = date;
        this.fuelTankFull = fuelTankFull;
    }

    public boolean isFuelTankFull() {
        return fuelTankFull;
    }

    public void setFuelTankFull(boolean fuelTankFull) {
        this.fuelTankFull = fuelTankFull;
    }

    public BigDecimal getPricePerLiter() {
        if (this.pricePerLiter == null) {
            this.pricePerLiter = BigDecimal.ZERO;
        }
        return pricePerLiter;
    }

    public void setPricePerLiter(BigDecimal pricePerLiter) {
        this.pricePerLiter = pricePerLiter;
    }

    public LocalDate getDate() {
        if (this.date == null) {
            this.date = LocalDate.now();
        }
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getLiters() {
        if (this.liters == null) {
            this.liters = BigDecimal.ZERO;
        }
        return liters;
    }

    public void setLiters(BigDecimal liters) {
        this.liters = liters;
    }

    private Bedrag getBedrag() {
        if (this.bedrag == null) {
            this.bedrag = new Bedrag(Currency.EUR);
        }
        return this.bedrag;
    }

    public void setCurrency(Currency currency) {
        this.getBedrag().setCurrency(currency);
    }

    public Currency getCurrency() {
        return this.getBedrag().getCurrency();
    }

    public BigDecimal getPrice() {
        return this.getBedrag().getValue();
    }

    public void setPrice(BigDecimal price) {
        this.getBedrag().setValue(price);
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public BigDecimal getKilometres() {
        if (this.kilometres == null) {
            this.kilometres = BigDecimal.ZERO;
        }
        return kilometres;
    }

    public void setKilometres(BigDecimal kilometres) {
        this.kilometres = kilometres;
    }
}
