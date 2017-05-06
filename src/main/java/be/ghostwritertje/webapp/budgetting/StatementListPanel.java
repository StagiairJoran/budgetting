package be.ghostwritertje.webapp.budgetting;

import be.ghostwritertje.domain.budgetting.Category;
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
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
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
public class StatementListPanel extends Panel {
    private static final long serialVersionUID = -7870855479329092357L;

    @SpringBean
    private StatementService statementService;
    @SpringBean
    private CategoryService categoryService;

    private final IModel<List<Statement>> selectedStatementsModel = new ListModel<>(new ArrayList<>());
    private final IModel<Category> categoryToAssignModel = new Model<>();


    private final StatementListContext statementListContext;

    StatementListPanel(String id, StatementListContext statementListContext) {
        super(id);
        this.setOutputMarkupId(true);

        this.statementListContext = statementListContext;
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
                .build("statements", this.statementListContext.getFilteredStatementListModel()));

        dataTableForm.add(checkGroup);
        LinkBuilderFactory.submitLink(submit())
                .usingDefaults()
                .attach(dataTableForm, "submit");
        this.add(dataTableForm);

        this.add(new StatementCriteriaPanel("criteriaPanel", this.statementListContext.getStatementCriteriaIModel(), this.statementListContext.getPersonModel(), filterStatements()));

        LinkBuilderFactory.ajaxLink(assignCategories())
                .usingDefaults()
                .attach(this, "assignCategories");
    }

    private static SerializableBiConsumer<AjaxRequestTarget, AjaxSubmitLink> filterStatements() {
        return (ajaxRequestTarget, components) -> {
            StatementListPanel parent = components.findParent(StatementListPanel.class);
            ajaxRequestTarget.add(parent);
        };
    }

    private static SerializableBiConsumer<AjaxRequestTarget, AjaxSubmitLink> submit() {
        return (ajaxRequestTarget, components) -> {
            StatementListPanel parent = components.findParent(StatementListPanel.class);
            List<Statement> statements = parent.selectedStatementsModel.getObject();
            statements.forEach(statement -> statement.setCategory(parent.categoryToAssignModel.getObject()));
            parent.statementService.save(statements);
            ajaxRequestTarget.add(parent);
        };
    }

    private static SerializableBiConsumer<AjaxRequestTarget, AjaxLink<Object>> assignCategories() {
        return (ajaxRequestTarget, components) -> {
            StatementListPanel parent = components.findParent(StatementListPanel.class);
            parent.categoryService.attemptToAssignCategoriesAutomaticallyForPerson(parent.statementListContext.getPersonModel().getObject());
            parent.statementListContext.getStatementListModel().setObject(null);
            ajaxRequestTarget.add(parent);
        };
    }
}
