package be.ghostwritertje.webapp.budgetting;

import be.ghostwritertje.domain.budgetting.Category;
import be.ghostwritertje.domain.budgetting.Statement;
import be.ghostwritertje.services.budgetting.CategoryService;
import be.ghostwritertje.services.budgetting.StatementService;
import be.ghostwritertje.webapp.datatable.ColumnBuilderFactory;
import be.ghostwritertje.webapp.datatable.DataTableBuilderFactory;
import be.ghostwritertje.webapp.form.BaseForm;
import be.ghostwritertje.webapp.form.FormComponentBuilderFactory;
import be.ghostwritertje.webapp.link.LinkBuilderFactory;
import be.ghostwritertje.webapp.model.DomainObjectListModel;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
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
    public static final String DATA_TABLE_FORM_ID = "dataTableForm";
    public static final String DATATABLE_ID = "statements";
    public static final String CHECK_GROUP_ID = "checkGroup";

    @SpringBean
    private StatementService statementService;
    @SpringBean
    private CategoryService categoryService;

    private final IModel<List<Statement>> selectedStatementsModel = new ListModel<>(new ArrayList<>());
    private final IModel<Category> categoryToAssignModel = new Model<>();
    private final IModel<List<Category>> categoryListModel;


    private final StatementListContext statementListContext;

    StatementListPanel(String id, StatementListContext statementListContext) {
        super(id);
        this.setOutputMarkupId(true);

        this.statementListContext = statementListContext;

        this.categoryListModel = new DomainObjectListModel<Category, CategoryService>(
                this.categoryService,
                service -> service.findByAdministrator(statementListContext.getPersonModel().getObject())
        );
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        LinkBuilderFactory.pageLink(() -> new StatementPage(new Model<>(new Statement()), this.statementListContext.getPersonModel()))
                .usingDefaults()
                .body(new ResourceModel("new"))
                .attach(this, "new");

        CheckGroup<Statement> checkGroup = new CheckGroup<Statement>(CHECK_GROUP_ID, this.selectedStatementsModel);

        BaseForm<List<Statement>> dataTableForm = new BaseForm<>(DATA_TABLE_FORM_ID, this.selectedStatementsModel);

        FormComponentBuilderFactory.<Category>dropDown()
                .usingDefaults()
                .body(new ResourceModel("category"))
                .attach(dataTableForm, "category", this.categoryToAssignModel, this.categoryListModel);

        checkGroup.add(DataTableBuilderFactory.<Statement, String>simple()
                .addColumn(ColumnBuilderFactory.<Statement, String>check().build(new ResourceModel("empty")))
                .addColumn(new LambdaColumn<>(new ResourceModel("from"), Statement::getOriginatingAccount))
                .addColumn(new LambdaColumn<>(new ResourceModel("date"), Statement::getDate))
                .addColumn(new LambdaColumn<>(new ResourceModel("amount"), Statement::getAmount))
                .addColumn(new LambdaColumn<>(new ResourceModel("description"), Statement::getDescription))
                .addColumn(new LambdaColumn<>(new ResourceModel("to"), Statement::getDestinationAccount))
                .addColumn(ColumnBuilderFactory.custom(new ResourceModel("category"), (s, statementIModel) -> new CategoryPanel(s, statementIModel, this.categoryListModel)))
                .build(DATATABLE_ID, this.statementListContext.getFilteredStatementListModel())
                .setOutputMarkupId(true));

        dataTableForm.add(checkGroup);
        LinkBuilderFactory.submitLink(submit())
                .usingDefaults()
                .attach(dataTableForm, "submit");
        this.add(dataTableForm);

        this.add(new StatementCriteriaPanel("criteriaPanel", this.statementListContext.getStatementCriteriaIModel(), this.statementListContext.getPersonModel(), filterStatements()));


    }

    @SuppressWarnings("unchecked")
    public BaseForm<List<Statement>> getForm() {
        return (BaseForm<List<Statement>>) this.get(DATA_TABLE_FORM_ID);
    }

    public Component getCheckGroup() {
        return this.getForm().get(CHECK_GROUP_ID);
    }

    public Component getDataTable() {
        return this.getCheckGroup().get(DATATABLE_ID);
    }

    private static SerializableBiConsumer<AjaxRequestTarget, AjaxSubmitLink> filterStatements() {
        return (ajaxRequestTarget, components) -> {
            StatementListPanel parent = components.findParent(StatementListPanel.class);
            parent.statementListContext.getFilteredStatementListModel().setObject(null);
            ajaxRequestTarget.add(parent);
        };
    }

    private static SerializableBiConsumer<AjaxRequestTarget, AjaxSubmitLink> submit() {
        return (ajaxRequestTarget, components) -> {
            StatementListPanel parent = components.findParent(StatementListPanel.class);
            List<Statement> statements = parent.selectedStatementsModel.getObject();
            statements.forEach(statement -> statement.setCategory(parent.categoryToAssignModel.getObject()));
            parent.statementService.save(statements);
            parent.getForm().getFormModeModel().setObject(BaseForm.FormMode.EDIT);
            ajaxRequestTarget.add(parent);
        };
    }

}
