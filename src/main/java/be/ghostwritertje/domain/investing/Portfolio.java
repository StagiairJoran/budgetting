package be.ghostwritertje.domain.investing;

import be.ghostwritertje.domain.DomainObject;
import be.ghostwritertje.domain.Person;
import be.ghostwritertje.utilities.Pair;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Jorandeboever
 * Date: 28-Dec-16.
 */
@Entity
@Table(name = "T_PORTFOLIO")
public class Portfolio extends DomainObject {

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "PORTFOLIO_ID")
    private final List<Allocation> allocationList = new ArrayList<>();

    private String name;

    @ManyToOne
    @JoinColumn(name = "PERSON_ID")
    private Person person;

    public Portfolio() {
    }

    public List<Pair<LocalDate, Double>> getValuesFromStartDate(LocalDate date) {
        Map<LocalDate, Value> list = new HashMap<>();
        this.allocationList.forEach(allocation -> allocation.getAllocationAdjustedValuesFromStartDate(date).forEach(localDateDoublePair -> {
            list.put(localDateDoublePair.getK(), Optional.ofNullable(list.get(localDateDoublePair.getK())).orElse(new Value()).add(localDateDoublePair.getV()));
        }));
        return list.entrySet().stream()
                .filter(entry-> entry.getValue().getCount() == allocationList.size())
                .map(entry -> new Pair<LocalDate, Double>(entry.getKey(), entry.getValue().getValue()))
                .sorted(Comparator.comparing((Function<Pair<LocalDate, Double>, LocalDate>) Pair::getK).reversed())
                .collect(Collectors.toList());
    }


    public List<Allocation> getAllocationList() {
        return allocationList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    private static class Value {
        int count = 0;
        Double value = 0.0;

        private Value add(Double value){
            this.value+= value;
            count++;
           return this;
        }

        public Double getValue() {
            return value;
        }

        private int getCount(){
            return count;
        }
    }
}
