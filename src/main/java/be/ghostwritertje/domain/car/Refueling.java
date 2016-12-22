package be.ghostwritertje.domain.car;

import be.ghostwritertje.domain.DomainObject;

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
    @JoinColumn(name = "car_id")
    private Car car;
    private LocalDate date;
    private Double liters;
    private Double price;
    private Double kilometres;
    private Double pricePerLiter;
    private boolean fuelTankFull;

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

    public Double getPrice() {
        if(this.price == null){
            this.price = 0.00;
        }
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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
