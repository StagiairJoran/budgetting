package be.ghostwritertje.webapp.budgetting;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.budgetting.Statement;
import be.ghostwritertje.services.budgetting.CategoryService;
import be.ghostwritertje.services.budgetting.StatementService;
import be.ghostwritertje.webapp.datatable.ColumnBuilderFactory;
import be.ghostwritertje.webapp.datatable.DataTableBuilderFactory;
import be.ghostwritertje.webapp.form.BaseForm;
import be.ghostwritertje.webapp.link.LinkBuilderFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.markup.html.repeater.data.table.LambdaColumn;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.danekja.java.util.function.serializable.SerializableBiConsumer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 01-May-17.
 */
public class StatementListPanel extends GenericPanel<Person> {
    private static final long serialVersionUID = -7870855479329092357L;

    @SpringBean
    private StatementService statementService;
    @SpringBean
    private CategoryService categoryService;

    private final IModel<List<Statement>> statementListModel;
    private final IModel<List<Statement>> selectedStatementsModel = new ListModel<>(new ArrayList<>());

    public StatementListPanel(String id, IModel<Person> personIModel, IModel<List<Statement>> statementListModel) {
        super(id, personIModel);


        this.statementListModel = statementListModel;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();


        CheckGroup<Statement> checkGroup = new CheckGroup<Statement>("checkGroup", this.selectedStatementsModel);

        BaseForm<List<Statement>> dataTableForm = new BaseForm<>("dataTableForm", this.selectedStatementsModel);

        checkGroup.add(DataTableBuilderFactory.<Statement, String>simple()
                .addColumn(ColumnBuilderFactory.<Statement, String>check().build(new ResourceModel("empty")))
                .addColumn(new LambdaColumn<>(new ResourceModel("from"), Statement::getOriginatingAccount))
                .addColumn(new LambdaColumn<>(new ResourceModel("date"), Statement::getDate))
                .addColumn(new LambdaColumn<>(new ResourceModel("amount"), Statement::getAmount))
                .addColumn(new LambdaColumn<>(new ResourceModel("description"), Statement::getDescription))
                .addColumn(new LambdaColumn<>(new ResourceModel("to"), Statement::getDestinationAccount))
                .addColumn(ColumnBuilderFactory.custom(new ResourceModel("category"), CategoryPanel::new))
                .build("statements", this.statementListModel));

        dataTableForm.add(checkGroup);
        LinkBuilderFactory.submitLink(submit())
                .usingDefaults()
                .attach(dataTableForm, "submit");
        this.add(dataTableForm);

        LinkBuilderFactory.ajaxLink(assignCategories())
                .usingDefaults()
                .attach(this, "assignCategories");
    }

    private static SerializableBiConsumer<AjaxRequestTarget, AjaxSubmitLink> submit() {
        return (ajaxRequestTarget, components) -> {
            StatementListPanel parent = components.findParent(StatementListPanel.class);
            parent.selectedStatementsModel.getObject().forEach(System.out::println);
        };
    }

    private static SerializableBiConsumer<AjaxRequestTarget, AjaxLink<Object>> assignCategories() {
        return (ajaxRequestTarget, components) -> {
            StatementListPanel parent = components.findParent(StatementListPanel.class);
            parent.categoryService.attemptToAssignCategoriesAutomaticallyForPerson(parent.getModelObject());
            parent.statementListModel.setObject(null);
            ajaxRequestTarget.add(parent);
        };
    }
}
