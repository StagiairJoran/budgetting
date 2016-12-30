package be.ghostwritertje.domain.car;

import be.ghostwritertje.domain.Bedrag;
import be.ghostwritertje.domain.Currency;
import be.ghostwritertje.domain.DomainObject;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
@Entity
@Table(name = "T_REFUELING")
public class Refueling extends DomainObject {

    @ManyToOne
    @JoinColumn(name = "car_UUID")
    private Car car;
    private LocalDate date;
    private Double liters;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(column = @Column(name = "price"), name = "value")
    })
    private Bedrag bedrag;
    private Double kilometres;
    private Double pricePerLiter;

    private boolean fuelTankFull = true;

    public Refueling() {
    }

    public boolean isFuelTankFull() {
        return fuelTankFull;
    }

    public void setFuelTankFull(boolean fuelTankFull) {
        this.fuelTankFull = fuelTankFull;
    }

    public Double getPricePerLiter() {
        if(this.pricePerLiter == null){
            this.pricePerLiter = 0.000;
        }
        return pricePerLiter;
    }

    public void setPricePerLiter(Double pricePerLiter) {
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

    public Double getLiters() {
        if (this.liters == null) {
            this.liters = 0.00;
        }
        return liters;
    }

    public void setLiters(Double liters) {
        this.liters = liters;
    }

    private Bedrag getBedrag() {
        if(this.bedrag == null){
            this.bedrag = new Bedrag(Currency.EUR);
        }
        return this.bedrag;
    }
    public void setCurrency(Currency currency) {
        this.getBedrag().setCurrency(currency);
    }

    public Currency getCurrency(){
        return this.getBedrag().getCurrency();
    }

    public Double getPrice() {
        return this.getBedrag().getValue();
    }

    public void setPrice(Double price) {
        this.getBedrag().setValue(price);
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Double getKilometres() {
        if(this.kilometres == null){
            this.kilometres = 0.00;
        }
        return kilometres;
    }

    public void setKilometres(Double kilometres) {
        this.kilometres = kilometres;
    }
}
