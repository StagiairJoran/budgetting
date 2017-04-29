package be.ghostwritertje.webapp.car.panel;

import be.ghostwritertje.domain.DomainObject;
import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.car.Car;
import be.ghostwritertje.services.car.CarService;
import be.ghostwritertje.services.person.PersonService;
import be.ghostwritertje.webapp.CustomSession;
import be.ghostwritertje.webapp.IModelBasedVisibilityBehavior;
import be.ghostwritertje.webapp.car.pages.RefuelingListPage;
import be.ghostwritertje.webapp.link.LinkBuilderFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.danekja.java.util.function.serializable.SerializableBiConsumer;

import java.util.Objects;
import java.util.Optional;

;

/**
 * Created by Jorandeboever
 * Date: 02-Oct-16.
 */
public class CarInfoPanel extends GenericPanel<Car> {
    private static final long serialVersionUID = 6528456929661772335L;

    @SpringBean
    private CarService carService;

    @SpringBean
    private PersonService personService;

    public CarInfoPanel(String id, IModel<Car> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        this.setOutputMarkupId(true);
        this.add(new Label("price", LambdaModel.of(this.getModel(), Car::getPurchasePrice)));

        LinkBuilderFactory.ajaxLink(makeFavourite())
                .usingDefaults()
                .behave(() -> new IModelBasedVisibilityBehavior<>(
                        this.getModel(),
                        car -> !Objects.equals(car.getUuid(), Optional.ofNullable(CustomSession.get().getLoggedInPerson().getFavouriteCar()).map(
                                DomainObject::getUuid).orElse(null))
                ))
                .attach(this, "favourite");

        LinkBuilderFactory.ajaxLink(getAjaxRequestTargetAjaxLinkSerializableBiConsumer())
                .usingDefaults()
                .behave(() -> new IModelBasedVisibilityBehavior<>(
                        this.getModel(),
                        car -> Objects.equals(car.getUuid(), Optional.ofNullable(CustomSession.get().getLoggedInPerson().getFavouriteCar()).map(
                                DomainObject::getUuid).orElse(null))
                ))
                .attach(this, "unFavourite");

        this.add(new Link<Car>("carLink", this.getModel()) {

            @Override
            protected void onInitialize() {
                super.onInitialize();
                this.add(new Label("brand", LambdaModel.of(this.getModel(), Car::getBrand)));
                this.add(new Label("model",LambdaModel.of(this.getModel(), Car::getModel)));
            }

            @Override
            public void onClick() {
                this.setResponsePage(new RefuelingListPage(this.getModel()));
            }
        });

        this.add(new Label("kilometresDriven", LambdaModel.of(this.getModel(), Car::getTotalKilometresDriven)));

        this.add(new Label("averageConsumption", LambdaModel.of(this.getModel(), Car::getAverageConsumption)));

        this.add(new Label("distancePerYear", LambdaModel.of(this.getModel(), Car::getAverageDistanceDrivenByYear)));

        this.add(new Label("averageFuelPrice", LambdaModel.of(this.getModel(), Car::getAverageFuelPrice)));

        this.add(new Label("costPerKilometer", LambdaModel.of(this.getModel(), Car::getCostPerKilometer)));

    }

    private static SerializableBiConsumer<AjaxRequestTarget, AjaxLink<Object>> getAjaxRequestTargetAjaxLinkSerializableBiConsumer() {
        return (target, components) -> {
            CarInfoPanel parent = components.findParent(CarInfoPanel.class);
            Person loggedInPerson = CustomSession.get().getLoggedInPerson();
            parent.carService.removeFavourite(parent.getModel().getObject(), loggedInPerson);
            CustomSession.get().setLoggedInPerson(parent.personService.findOne(loggedInPerson.getUuid()));
            target.add(parent);

        };
    }

    private static SerializableBiConsumer<AjaxRequestTarget, AjaxLink<Object>> makeFavourite() {
        return (target, components) -> {
            CarInfoPanel parent = components.findParent(CarInfoPanel.class);
            Person loggedInPerson = CustomSession.get().getLoggedInPerson();
            parent.carService.makeFavourite(parent.getModel().getObject(), loggedInPerson);
            CustomSession.get().setLoggedInPerson(parent.personService.findOne(loggedInPerson.getUuid()));
            target.add(parent);
        };
    }
}
