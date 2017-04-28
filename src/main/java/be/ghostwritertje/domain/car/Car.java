package be.ghostwritertje.domain.car;

import be.ghostwritertje.domain.DomainObject;
import be.ghostwritertje.domain.Person;
import org.hibernate.annotations.Formula;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

    @Formula(value = "(SELECT MAX(t1.KILOMETRES) - MIN(t1.KILOMETRES) FROM T_REFUELING t1 WHERE t1.CAR_UUID = UUID)")
    private BigDecimal kilometresDriven;

    @Formula(value = "(SELECT SUM(T1.LITERS) - (SELECT t2.LITERS FROM t_refueling t2 WHERE  t2.CAR_UUID = UUID AND t2.DATE = (SELECT MIN(t3.DATE) FROM t_refueling t3 WHERE t3.CAR_UUID = UUID))\n" +
            "          FROM T_REFUELING T1\n" +
            "          WHERE\n" +
            "            T1.CAR_UUID = UUID\n" +
            "           ) ")
    private BigDecimal totalLitersConsumed;

    public Person getOwner() {
        return this.owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public String getBrand() {
        return this.brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public LocalDate getPurchaseDate() {
        return this.purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public FuelType getFuelType() {
        return this.fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public enum FuelType {
        DIESEL, BENZINE
    }

    public Double getPurchasePrice() {
        return this.purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getAverageConsumption() {
        return this.totalLitersConsumed.divide(this.kilometresDriven, 4, RoundingMode.HALF_DOWN).multiply(new BigDecimal("100"));
    }

    public BigDecimal getKilometresDriven() {
        return this.kilometresDriven;
    }
}
