package be.ghostwritertje.webapp.car.pages;

import be.ghostwritertje.domain.car.Car;
import be.ghostwritertje.domain.car.Refueling;
import be.ghostwritertje.services.car.RefuelingSearchResult;
import be.ghostwritertje.services.car.RefuelingService;
import be.ghostwritertje.webapp.charts.ChartBuilderFactory;
import be.ghostwritertje.webapp.model.DomainObjectListModel;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class RefuelingChartsPanel  extends GenericPanel<Car>{
    private static final long serialVersionUID = -4511171471439021478L;

    @SpringBean
    private RefuelingService refuelingService;

    public RefuelingChartsPanel(String id, IModel<Car> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        IModel<List<Refueling>> someModel = new DomainObjectListModel<>(this.refuelingService, s -> s.findByCar(this.getModelObject()));

        ChartBuilderFactory.splineChart()
                .title("Dieselprijs")
                .addLine("Kostprijs diesel", someModel.getObject(), Refueling::getDate, Refueling::getPricePerLiter, 3)
                .setYAxis("Price/liter")
                .attach(this, "chart1");


        List<RefuelingSearchResult> coordinates = this.refuelingService.mapRefuelingsToSearchResults(this.refuelingService.findByCar(this.getModelObject()));
        ChartBuilderFactory.splineChart()
                .title("Verloop van verbruik")
                .addLine(
                        "Verbruik",
                        coordinates,
                        refuelingSearchResult -> refuelingSearchResult.getRefueling().getDate(),
                        RefuelingSearchResult::getConsumption,
                        2,
                        this.getModelObject().getAverageConsumption()
                )
                .setYAxis("liter/100km")

                .attach(this, "chart2");

    }
}
