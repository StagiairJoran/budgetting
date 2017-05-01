package be.ghostwritertje.webapp.budgetting;

import be.ghostwritertje.domain.budgetting.Bank;
import be.ghostwritertje.domain.budgetting.BankAccount;
import be.ghostwritertje.domain.budgetting.Category;
import be.ghostwritertje.domain.budgetting.Statement;
import be.ghostwritertje.services.budgetting.CategoryService;
import be.ghostwritertje.services.budgetting.StatementService;
import be.ghostwritertje.services.budgetting.csv.CsvService;
import be.ghostwritertje.webapp.BasePage;
import be.ghostwritertje.webapp.form.BaseForm;
import be.ghostwritertje.webapp.form.FormComponentBuilderFactory;
import be.ghostwritertje.webapp.link.LinkBuilderFactory;
import be.ghostwritertje.webapp.model.DomainObjectListModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.file.File;
import org.danekja.java.util.function.serializable.SerializableBiConsumer;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
public class StatementListPage extends BasePage<BankAccount> {
    public static final String FORM_ID = "form";
    @SpringBean
    private StatementService statementService;

    @SpringBean
    private CategoryService categoryService;

    private static final Logger LOG = LogManager.getLogger();

    @SpringBean
    private CsvService csvService;

    private final IModel<List<FileUpload>> fileUploadModel;
    private final IModel<List<Statement>> statementListModel;

    private final IModel<Category> categoryToAssignModel = new Model<>();

    private static final String UPLOAD_FOLDER = "csvFiles";

    public StatementListPage(IModel<BankAccount> model) {
        super(model);
        this.fileUploadModel = new ListModel<>();

        this.statementListModel = new DomainObjectListModel<>(
                this.statementService,
                service -> service.findByOriginatingAccount(this.getModelObject())
        );

    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        this.add(new Label("titel", LambdaModel.of(this.getModel(), BankAccount::getName)));



        BaseForm<BankAccount> form = new BaseForm<>(FORM_ID, this.getModel());
        FileUploadField fileUpload = new FileUploadField("fileUpload", this.fileUploadModel);

        FormComponentBuilderFactory.<Bank>dropDown()
                .usingDefaults()
                .body(new ResourceModel("bank"))
                .attach(form, "bankType", LambdaModel.of(this.getModel(), BankAccount::getBank, BankAccount::setBank), () -> CsvService.SUPPORTED_BANKS);

        LinkBuilderFactory.<List<FileUpload>>submitLink(upload())
                .usingDefaults()
                .attach(form, "upload");

        form.add(fileUpload);

        this.add(form);


        this.add(new StatementListPanel("statementList", this.getModel(), this.statementListModel));
    }



    @SuppressWarnings("unchecked")
    public BaseForm<BankAccount> getForm() {
        return (BaseForm<BankAccount>) this.get(FORM_ID);
    }

    private static SerializableBiConsumer<AjaxRequestTarget, AjaxSubmitLink> upload() {
        return (ajaxRequestTarget, components) -> {
            StatementListPage parent = components.findParent(StatementListPage.class);
            Collection<FileUpload> uploadedFiles = parent.fileUploadModel.getObject();

            for (FileUpload uploadedFile : uploadedFiles) {
                if (uploadedFile != null) {

                    // write to a new file
                    File newFile = new File(UPLOAD_FOLDER + UUID.randomUUID()
                            + uploadedFile.getClientFileName());

                    if (newFile.exists()) {
                        newFile.delete();
                    }

                    try {
                        newFile.createNewFile();
                        uploadedFile.writeTo(newFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                        LOG.error(String.format("Error uploading file %s", e));
                    }
                    components.info("saved file: " + uploadedFile.getClientFileName());
                    parent.csvService.uploadCSVFile(newFile.getAbsolutePath(), parent.getModelObject());
                    parent.statementListModel.setObject(null);
                    parent.getForm().getFormModeModel().setObject(BaseForm.FormMode.EDIT);
                    ajaxRequestTarget.add(parent);

                }

            }
        };
    }


}
