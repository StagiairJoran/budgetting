package be.ghostwritertje.webapp.car.panel;

import be.ghostwritertje.domain.car.Car;
import be.ghostwritertje.webapp.car.pages.RefuelingListPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;

/**
 * Created by Jorandeboever
 * Date: 02-Oct-16.
 */
public class CarInfoPanel extends GenericPanel<Car> {

    public CarInfoPanel(String id, IModel<Car> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        this.add(new Label("price", new LambdaModel<>(() -> this.getModelObject().getPurchasePrice(), (b) -> this.getModelObject().setPurchasePrice(b))));

        this.add(new Link<Car>("carLink", this.getModel()) {

            @Override
            protected void onInitialize() {
                super.onInitialize();
                this.add(new Label("brand", new LambdaModel<>(() -> this.getModelObject().getBrand(), (b) -> this.getModelObject().setBrand(b))));
                this.add(new Label("model", new LambdaModel<>(() -> this.getModelObject().getModel(), (b) -> this.getModelObject().setModel(b))));
            }

            @Override
            public void onClick() {
                this.setResponsePage(new RefuelingListPage(this.getModel()));
            }
        });
    }
}
