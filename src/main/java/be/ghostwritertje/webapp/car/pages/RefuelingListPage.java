package be.ghostwritertje.webapp.car.pages;

import be.ghostwritertje.domain.car.Car;
import be.ghostwritertje.domain.car.Refueling;
import be.ghostwritertje.services.car.RefuelingSearchResult;
import be.ghostwritertje.services.car.RefuelingService;
import be.ghostwritertje.webapp.BasePage;
import be.ghostwritertje.webapp.car.panel.CarInfoPanel;
import be.ghostwritertje.webapp.charts.ChartBuilderFactory;
import be.ghostwritertje.webapp.datatable.CheckBoxColumn;
import be.ghostwritertje.webapp.datatable.ColumnBuilderFactory;
import be.ghostwritertje.webapp.datatable.DataTableBuilderFactory;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
public class RefuelingListPage extends BasePage<Car> {

    @SpringBean
    private RefuelingService refuelingService;

    private final IModel<List<Refueling>> refuelingListModel;

    public RefuelingListPage(IModel<Car> model) {
        super(model);
        this.refuelingListModel = new ListModel<>(this.refuelingService.findByCar(this.getModelObject()));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();


        this.add(new CarInfoPanel("carInfo", this.getModel()));

        this.add(DataTableBuilderFactory.<Refueling, String>simple()
                .addColumn(ColumnBuilderFactory.<Refueling, String>simple( Refueling::getDate).build(new ResourceModel("date")))
                .addColumn(ColumnBuilderFactory.<Refueling, String>simple( Refueling::getKilometres).build(new ResourceModel("kilometres")))
                .addColumn(ColumnBuilderFactory.<Refueling, String>simple( Refueling::getLiters).hideOnMobile().build(new ResourceModel("liters")))
                .addColumn(ColumnBuilderFactory.<Refueling, String>simple( Refueling::getPrice).hideOnMobile().build(new ResourceModel("price")))
                .addColumn(ColumnBuilderFactory.<Refueling, String>simple( Refueling::getPricePerLiter).build(new ResourceModel("price.per.liter")))
                .addColumn(new CheckBoxColumn<>(new ResourceModel("fuel.tank.full"), Refueling::isFuelTankFull))
                .addColumn(ColumnBuilderFactory.actions(new ResourceModel("actions"),
                        (target, link) -> this.setResponsePage(new RefuelingPage(link.getModel())),
                        (target, link) -> {
                            RefuelingListPage.this.refuelingService.delete(link.getModelObject());
                            this.setResponsePage(new RefuelingListPage(this.getModel()));
                        })
                )
                .build("dataTable", refuelingListModel));

        this.add(new Link<Refueling>("newRefuelingLink") {
            @Override
            public void onClick() {
                Refueling refueling = new Refueling();
                refueling.setCar(RefuelingListPage.this.getModelObject());
                refueling.setDate(LocalDate.now());
                this.setResponsePage(new RefuelingPage(new Model<Refueling>(refueling)));
            }
        });

        ChartBuilderFactory.splineChart()
                .usingDefaults()
                .title("Dieselprijs")
                .addLine("Kostprijs diesel", this.refuelingService.findByCar(this.getModelObject()), Refueling::getDate, Refueling::getPricePerLiter, 3)
                .setYAxis("Price/liter")
                .attach(this, "chart");

        ChartBuilderFactory.splineChart()
                .usingDefaults()
                .title("Verloop van verbruik")
                .addLine("Verbruik", this.refuelingService.mapRefuelingsToSearchResults(this.refuelingService.findByCar(this.getModelObject())), refuelingSearchResult -> refuelingSearchResult.getRefueling().getDate(), RefuelingSearchResult::getConsumption, 2)
                .setYAxis("liter/100km")
                .attach(this, "chart2");


        ChartBuilderFactory.splineChart()
                .usingDefaults()
                .title("Driven per month")
                .addLine("Kilometres", this.refuelingService.mapRefuelingsToSearchResults(this.refuelingService.findByCar(this.getModelObject())), refueling -> refueling.getRefueling().getDate(), RefuelingSearchResult::getKilometresPerMonth, 0)
                .setYAxis("kilometres/month")
                .attach(this, "chart3");

    }
}
