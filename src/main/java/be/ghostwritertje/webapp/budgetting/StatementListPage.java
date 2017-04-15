package be.ghostwritertje.webapp.budgetting;

import be.ghostwritertje.domain.budgetting.BankAccount;
import be.ghostwritertje.domain.budgetting.Statement;
import be.ghostwritertje.services.budgetting.CsvService;
import be.ghostwritertje.services.budgetting.StatementService;
import be.ghostwritertje.webapp.BasePage;
import be.ghostwritertje.webapp.datatable.DataTableBuilderFactory;
import be.ghostwritertje.webapp.model.DomainObjectListModel;
import org.apache.wicket.extensions.markup.html.repeater.data.table.LambdaColumn;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.file.File;

import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
public class StatementListPage extends BasePage<BankAccount> {
    @SpringBean
    private StatementService statementService;

    @SpringBean
    private CsvService csvService;


    private final IModel<? extends List<FileUpload>> fileUploadModel;
    private static final String UPLOAD_FOLDER = "csvFiles";

    public StatementListPage(IModel<BankAccount> model) {
        super(model);
        this.fileUploadModel = new ListModel<>();
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        this.add(DataTableBuilderFactory.<Statement, String>simple()
                .addColumn(new LambdaColumn<>(new ResourceModel("date"), Statement::getDate))
                .addColumn(new LambdaColumn<>(new ResourceModel("amount"), Statement::getAmount))
                .addColumn(new LambdaColumn<>(new ResourceModel("from"), Statement::getOriginatingAccount))
                .addColumn(new LambdaColumn<>(new ResourceModel("to"), Statement::getDestinationAccount))
                .build("statements", new DomainObjectListModel<>(this.statementService, service -> service.findByOriginatingAccount(this.getModelObject()))));

        FileUploadField fileUpload = new FileUploadField("fileUpload", this.fileUploadModel);

        Form<BankAccount> form = new Form<BankAccount>("form", this.getModel()) {
            @Override
            protected void onSubmit() {

                final FileUpload uploadedFile = fileUpload.getFileUpload();
                if (uploadedFile != null) {

                    // write to a new file
                    File newFile = new File(UPLOAD_FOLDER
                            + uploadedFile.getClientFileName());

                    if (newFile.exists()) {
                        newFile.delete();
                    }

                    try {
                        newFile.createNewFile();
                        uploadedFile.writeTo(newFile);

                        info("saved file: " + uploadedFile.getClientFileName());
                        csvService.uploadCSVFile(newFile.getAbsolutePath(), this.getModelObject(), "keytrade");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        };
        form.add(fileUpload);

        this.add(form);
    }
}
