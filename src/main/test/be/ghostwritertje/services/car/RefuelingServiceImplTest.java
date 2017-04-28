package be.ghostwritertje.services.car;


import be.ghostwritertje.domain.car.Refueling;
import be.ghostwritertje.repository.RefuelingDao;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 28-Apr-17.
 */
public class RefuelingServiceImplTest extends MockitoServiceTest {

    private static final double AVG_DAYS_PER_MONTH_TEST = 365.25 / 12;

    @Mock
    private RefuelingDao refuelingDao;

    @Test
    public void mapRefuelingsToSearchResults() {
        List<Refueling> refuelingList = new ArrayList<>();

        refuelingList.add(new Refueling(new BigDecimal("62.0"), new BigDecimal("1240.0"), LocalDate.of(2016, 2, 1)));
        refuelingList.add(new Refueling(BigDecimal.ZERO, BigDecimal.ZERO, LocalDate.of(2016, 1, 1)));

        List<RefuelingSearchResult> result = this.getService().mapRefuelingsToSearchResults(refuelingList);

        Assertions.assertThat(result).hasSize(1)
                .extracting(
                        refuelingSearchResult -> refuelingSearchResult.getConsumption().setScale(2, RoundingMode.HALF_DOWN),
                        refuelingSearchResult1 -> refuelingSearchResult1.getKilometresPerMonth().setScale(2, RoundingMode.HALF_DOWN)
                )
                .contains(Tuple.tuple(new BigDecimal("5.00"), new BigDecimal("1217.50")));
    }


    @Test
    public void mapRefuelingsToSearchResults_incomplete() {
        List<Refueling> refuelingList = new ArrayList<>();

        refuelingList.add(new Refueling(new BigDecimal("31.0"), new BigDecimal("1240.0"), LocalDate.of(2016, 2, 1), true));
        refuelingList.add(new Refueling(new BigDecimal("0.0"), new BigDecimal("0.0"), LocalDate.of(2016, 1, 1), true));
        refuelingList.add(new Refueling(new BigDecimal("31.0"), new BigDecimal("650.0"), LocalDate.of(2016, 1, 15), false));

        List<RefuelingSearchResult> result = this.getService().mapRefuelingsToSearchResults(refuelingList);

        Assertions.assertThat(result).hasSize(2)
                .extracting(
                        refuelingSearchResult -> refuelingSearchResult.getConsumption().setScale(2, RoundingMode.HALF_DOWN),
                        refuelingSearchResult1 -> refuelingSearchResult1.getKilometresPerMonth().setScale(2, RoundingMode.HALF_DOWN)
                )
                .contains(Tuple.tuple(new BigDecimal("5.00"), new BigDecimal("1217.50")));
    }


    @Test
    public void mapRefuelingsToSearchResults_incomplete2() {
        List<Refueling> refuelingList = new ArrayList<>();

        refuelingList.add(new Refueling(new BigDecimal("0.0"), new BigDecimal("0.0"), LocalDate.of(2016, 1, 1), true));
        refuelingList.add(new Refueling(new BigDecimal("62.0"), new BigDecimal("1240.0"), LocalDate.of(2016, 2, 1), true));

        List<RefuelingSearchResult> result = this.getService().mapRefuelingsToSearchResults(refuelingList);

        Assertions.assertThat(result).hasSize(1)
                .extracting(
                        refuelingSearchResult -> refuelingSearchResult.getConsumption().setScale(2, RoundingMode.HALF_DOWN),
                        refuelingSearchResult1 -> refuelingSearchResult1.getKilometresPerMonth().setScale(2, RoundingMode.HALF_DOWN)
                )
                .contains(Tuple.tuple(new BigDecimal("5.00"), new BigDecimal("1217.50")));
    }


    private RefuelingServiceImpl getService() {
        return new RefuelingServiceImpl(this.refuelingDao);
    }

}