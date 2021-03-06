package be.ghostwritertje.domain.investing;

import be.ghostwritertje.domain.DomainObject;
import be.ghostwritertje.domain.Person;
import be.ghostwritertje.utilities.Pair;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Jorandeboever
 * Date: 28-Dec-16.
 */
@Entity
@Table(name = "T_PORTFOLIO")
public class Portfolio extends DomainObject {
    private static final long serialVersionUID = 7536007246538849261L;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "PORTFOLIO_UUID")
    private final List<Allocation> allocationList = new ArrayList<>();

    private String name;

    @ManyToOne
    @JoinColumn(name = "PERSON_UUID")
    private Person person;

    public Portfolio() {
    }

    public List<Pair<LocalDate, BigDecimal>> getValuesFromStartDate(LocalDate date) {
        Map<LocalDate, Value> list = new HashMap<>();
        this.allocationList.forEach(allocation -> allocation.getAllocationAdjustedValuesFromStartDate(date).forEach(localDateDoublePair -> {
            list.put(localDateDoublePair.getK(), Optional.ofNullable(list.get(localDateDoublePair.getK())).orElse(new Value()).add(localDateDoublePair.getV()));
        }));
        return list.entrySet().stream()
                .filter(entry -> entry.getValue().getCount() == this.allocationList.size())
                .map(entry -> new Pair<LocalDate, BigDecimal>(entry.getKey(), entry.getValue().getValue()))
                .sorted(Comparator.comparing((Function<Pair<LocalDate, BigDecimal>, LocalDate>) Pair::getK).reversed())
                .collect(Collectors.toList());
    }


    public List<Allocation> getAllocationList() {
        return this.allocationList;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    private static class Value {
        int count = 0;
        BigDecimal value = BigDecimal.ZERO;

        private Value add(BigDecimal value) {
            this.value = this.value.add(value);
            this.count++;
            return this;
        }

        public BigDecimal getValue() {
            return this.value;
        }

        private int getCount() {
            return this.count;
        }
    }
}
