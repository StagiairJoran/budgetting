package be.ghostwritertje.domain;

import be.ghostwritertje.domain.car.Car;

import javax.persistence.*;

/**
 * Created by Ghostwritertje
 * Date: 29-Sep-16.
 */
@Table(name = "T_PERSON")
@Entity
public class Person extends DomainObject {

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false, name = "password")
    private String password;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FAVOURITE_CAR_UUID")
    private Car favouriteCar;

    public Person() {
    }

    public Person(String username) {
        this.username = username;
    }

    public String getUsername() {
        if (this.username == null) {
            this.username = "";
        }
        return username;
    }

    public Car getFavouriteCar() {
        return this.favouriteCar;
    }

    public void setFavouriteCar(Car favouriteCar) {
        this.favouriteCar = favouriteCar;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
