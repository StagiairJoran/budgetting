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
import java.time.temporal.ChronoUnit;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
@Entity
@Table(name = "T_CAR")
public class Car extends DomainObject {
    private static final long serialVersionUID = 3043871930212926434L;

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

    /**
     * Total of liters consumed as a result of personal driving.
     * The initial refueling is not counted (since this is before haven driven any distance)
     */
    @Formula(value = "(SELECT SUM(T1.LITERS) - (SELECT T2.LITERS FROM T_REFUELING T2 WHERE  T2.CAR_UUID = UUID AND T2.DATE = (SELECT MIN(T3.DATE) FROM T_REFUELING T3 WHERE T3.CAR_UUID = UUID))\n" +
            "          FROM T_REFUELING T1\n" +
            "          WHERE\n" +
            "            T1.CAR_UUID = UUID\n" +
            "           ) ")
    private BigDecimal totalLitersConsumed;

    /**
     * Total amount payed for fuel as a result of personal driving.
     * The initial refueling is not counted (since this is before haven driven any distance)
     */
    @Formula(value = "(SELECT SUM(T1.PRICE) - (SELECT T2.PRICE FROM T_REFUELING T2 WHERE  T2.CAR_UUID = UUID AND T2.DATE = (SELECT MIN(T3.DATE) FROM T_REFUELING T3 WHERE T3.CAR_UUID = UUID))\n" +
            "          FROM T_REFUELING T1\n" +
            "          WHERE\n" +
            "            T1.CAR_UUID = UUID\n" +
            "           ) ")
    private BigDecimal totalPayedForFuel;

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

    public BigDecimal getTotalKilometresDriven() {
        return this.kilometresDriven;
    }

    public BigDecimal getTotalLitersConsumed() {
        return this.totalLitersConsumed;
    }

    public BigDecimal getTotalPayedForFuel() {
        return this.totalPayedForFuel;
    }

    public BigDecimal getAverageDistanceDrivenByYear(){
        return this.getTotalKilometresDriven().divide(BigDecimal.valueOf(this.purchaseDate.until(LocalDate.now(), ChronoUnit.DAYS)), 4, RoundingMode.HALF_DOWN)
                .multiply(new BigDecimal("365.25"))
                .setScale(0, RoundingMode.HALF_DOWN);
    }

    public BigDecimal getAverageFuelPrice(){
        return this.getTotalPayedForFuel().divide(this.getTotalLitersConsumed(), 10, RoundingMode.HALF_DOWN);
    }

    public BigDecimal getCostPerKilometer(){
        return this.getAverageConsumption().multiply(this.getAverageFuelPrice()).divide(new BigDecimal("100"), 3, RoundingMode.HALF_DOWN);
    }
}
