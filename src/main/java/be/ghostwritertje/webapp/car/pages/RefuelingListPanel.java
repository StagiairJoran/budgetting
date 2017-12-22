package be.ghostwritertje.webapp.car.pages;

import be.ghostwritertje.domain.car.Car;
import be.ghostwritertje.domain.car.Refueling;
import be.ghostwritertje.services.car.RefuelingService;
import be.ghostwritertje.webapp.datatable.ColumnBuilderFactory;
import be.ghostwritertje.webapp.datatable.DataTableBuilderFactory;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.time.LocalDate;
import java.util.List;

public class RefuelingListPanel extends GenericPanel<Car>{
    private static final long serialVersionUID = -1378148410430398716L;

    @SpringBean
    @SuppressWarnings({"UnusedDeclaration"})
    private RefuelingService refuelingService;

    private final IModel<List<Refueling>> refuelingListModel;

    public RefuelingListPanel(String id, IModel<Car> model) {
        super(id, model);

        this.refuelingListModel = new ListModel<>(this.refuelingService.findByCar(this.getModelObject()));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        this.add(DataTableBuilderFactory.<Refueling, String>simple()
                .addColumn(ColumnBuilderFactory.<Refueling, String>simple(Refueling::getDate).build(new ResourceModel("date")))
                .addColumn(ColumnBuilderFactory.<Refueling, String>simple(Refueling::getKilometres).build(new ResourceModel("kilometres")))
                .addColumn(ColumnBuilderFactory.<Refueling, String>simple(Refueling::getLiters).hideOnMobile().build(new ResourceModel("liters")))
                .addColumn(ColumnBuilderFactory.<Refueling, String>simple(Refueling::getPrice).hideOnMobile().build(new ResourceModel("price")))
                .addColumn(ColumnBuilderFactory.<Refueling, String>simple(Refueling::getPricePerLiter).build(new ResourceModel("price.per.liter")))
//                .addColumn(new CheckBoxColumn<>(new ResourceModel("fuel.tank.full"), Refueling::isFuelTankFull))
                .addColumn(ColumnBuilderFactory.actions(
                        new ResourceModel("actions"),
                        (target, link) -> this.setResponsePage(new RefuelingPage(link.getModel())),
                        (target, link) -> {
                            RefuelingListPanel.this.refuelingService.delete(link.getModelObject());
                            this.setResponsePage(new RefuelingListPage(this.getModel()));
                        }
                        )
                )
                .build("dataTable", refuelingListModel));

        this.add(new Link<Refueling>("newRefuelingLink") {
            @Override
            public void onClick() {
                Refueling refueling = new Refueling();
                refueling.setCar(RefuelingListPanel.this.getModelObject());
                refueling.setDate(LocalDate.now());
                this.setResponsePage(new RefuelingPage(new Model<Refueling>(refueling)));
            }
        });
    }
}
