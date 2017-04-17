package be.ghostwritertje.domain.car;

import be.ghostwritertje.domain.DomainObject;
import be.ghostwritertje.domain.Person;
import org.hibernate.annotations.Formula;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
@Entity
@Table(name = "T_CAR")
public class Car extends DomainObject {

    @ManyToOne
    @JoinColumn(name = "owner_UUID")
    private Person owner;
    private String brand;
    private String model;
    private LocalDate purchaseDate;
    private FuelType fuelType;
    private Double purchasePrice;

    @Formula(value = "( 100* (SELECT SUM(T1.LITERS)\n" +
            "   FROM T_REFUELING T1\n" +
            "   WHERE\n" +
            "     T1.CAR_UUID = UUID AND\n" +
            "     T1.KILOMETRES != (SELECT MIN(T3.KILOMETRES)\n" +
            "                          FROM T_REFUELING T3\n" +
            "     WHERE T3.CAR_UUID = UUID)) / (SELECT MAX(T2.KILOMETRES) - MIN(T2.KILOMETRES)\n" +
            "                                               FROM T_REFUELING T2\n" +
            "  WHERE T2.CAR_UUID = UUID )) ")
    private BigDecimal averageConsumption;

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public enum FuelType {
        DIESEL, BENZINE
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getAverageConsumption() {
        return this.averageConsumption;
    }

    public void setAverageConsumption(BigDecimal averageConsumption) {
        this.averageConsumption = averageConsumption;
    }
}
