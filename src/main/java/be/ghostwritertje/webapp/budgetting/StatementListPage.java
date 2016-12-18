package be.ghostwritertje.webapp.budgetting;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.budgetting.Statement;
import be.ghostwritertje.services.budgetting.CsvService;
import be.ghostwritertje.services.budgetting.StatementService;
import be.ghostwritertje.webapp.BasePage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.file.File;

import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
public class StatementListPage extends BasePage<Person> {
    @SpringBean
    private StatementService statementService;

    @SpringBean
    private CsvService csvService;

    private final IModel<? extends List<FileUpload>> fileUploadModel;
    private static final String UPLOAD_FOLDER = "csvFiles";

    public StatementListPage(IModel<Person> model) {
        super(model);
        this.fileUploadModel = new ListModel<>();
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        this.add(new ListView<Statement>("statements", this.statementService.findAll(this.getModelObject())) {
            @Override
            protected void onInitialize() {
                super.onInitialize();
                this.setViewSize(25);
            }

            @Override
            protected void populateItem(ListItem<Statement> item) {
                item.add(new Label("originatingAccount", item.getModelObject().getOriginatingAccount().getNumber()));
                item.add(new Label("destinationAccount", item.getModelObject().getDestinationAccount().getNumber()));
                item.add(new Label("amount", item.getModelObject().getAmount()));
                item.add(new Label("date", item.getModelObject().getDate()));
            }
        });

        FileUploadField fileUpload = new FileUploadField("fileUpload", this.fileUploadModel);

        Form<Person> form = new Form<Person>("form", this.getModel()) {
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
                        csvService.uploadCSVFile(newFile.getAbsolutePath(), this.getModelObject());
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
