package be.ghostwritertje.services.car;

import be.ghostwritertje.domain.car.Refueling;
import be.ghostwritertje.repository.RefuelingDao;
import org.junit.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Jorandeboever
 * Date: 21-Dec-16.
 */
public class RefuelingServiceImplTest {

    @Mock
    private RefuelingDao dao;


    @Test
    public void mapRefuelingsToSearchResults_kilometresPerMonth() throws Exception {
        Refueling refueling1 = new Refueling();
        refueling1.setDate(LocalDate.of(2015,1,1));
        refueling1.setKilometres(0.0);
        Refueling refueling2 = new Refueling();
        refueling2.setDate(LocalDate.of(2016,1,1));
        refueling2.setKilometres(12000.0);

        List<RefuelingSearchResult> results = this.getService().mapRefuelingsToSearchResults(Arrays.asList(refueling1, refueling2));

        assertEquals(1,results.size());
        assertEquals((Double) 1000.0, (Double) results.get(0).getKilometresPerMonth());
    }

    @Test
    public void mapRefuelingsToSearchResults_kilometresPerMonth2() throws Exception {
        Refueling refueling1 = new Refueling();
        refueling1.setDate(LocalDate.of(2016,1,1));
        refueling1.setKilometres(1250.0);
        Refueling refueling2 = new Refueling();
        refueling2.setDate(LocalDate.of(2016,1,25));
        refueling2.setKilometres(12000.0);

        List<RefuelingSearchResult> results = this.getService().mapRefuelingsToSearchResults(Arrays.asList(refueling1, refueling2));

        assertEquals(1,results.size());
        assertEquals((Double) 1000.0, (Double) results.get(0).getKilometresPerMonth());
    }

    @Test
    public void mapRefuelingsToSearchResults_consumption() throws Exception {
        Refueling refueling1 = new Refueling();
        refueling1.setLiters(35.32);
        refueling1.setKilometres(0.0);
        Refueling refueling2 = new Refueling();
        refueling2.setLiters(50.0);
        refueling2.setKilometres(1000.0);

        List<RefuelingSearchResult> results = this.getService().mapRefuelingsToSearchResults(Arrays.asList(refueling1, refueling2));

        assertEquals(1,results.size());
        assertEquals((Double)5.0, results.get(0).getConsumption());
    }

    @Test
    public  void someTest() {
       LocalDate begin =   LocalDate.of(2016,1,1);
       LocalDate end = LocalDate.of(2016,3,25);

       long numberOfDays = begin.until(end, ChronoUnit.DAYS);

       assertEquals( 70, numberOfDays);
    }

    private RefuelingService getService(){
        return new RefuelingServiceImpl(this.dao);
    }

}