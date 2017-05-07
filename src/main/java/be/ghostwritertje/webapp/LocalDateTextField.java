package be.ghostwritertje.webapp;

import be.ghostwritertje.utilities.DateUtilities;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextField;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextFieldConfig;
import org.apache.log4j.Logger;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.DateConverter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
public class LocalDateTextField extends DateTextField {
    private static final Logger logger = Logger.getLogger(LocalDateTextField.class);

    public LocalDateTextField(String id, IModel<LocalDate> model) {
        super(id, LambdaModel.of(
                () -> {
                    logger.debug(String.format("GETTER for dateTextField:"));
                   return Optional.ofNullable(model.getObject())
                            .map(localDate -> {
                                logger.debug(String.format("Date in localDate format: %s", localDate.toString()));
                                Date utilDate = DateUtilities.toUtilDate(localDate);
                                logger.debug(String.format("Date in util format: %s", utilDate.toString()));
                                return utilDate;
                            }).orElse(null);
                },
                date -> {
                    logger.debug(String.format("SETTER for dateTextField:"));
                    logger.debug(String.format("Date in util format: %s", date.toString()));
                    LocalDate localDate = DateUtilities.toLocalDate(date);
                    logger.debug(String.format("Date in localDate format: %s", localDate.toString()));
                    model.setObject(localDate);
                }), new DateTextFieldConfig()
                    .withFormat("dd-MM-yyyy")
        );//"dd-MM-yyyy"

    }

    @SuppressWarnings("unchecked")
    @Override //TODO reversed dateformat = louche
    public <C> IConverter<C> getConverter(Class<C> type) {
        if (Date.class.isAssignableFrom(type)) {
            return (IConverter<C>) new DateConverter() {
                private static final long serialVersionUID = 1L;

                /**
                 * @see DateConverter#getDateFormat(Locale)
                 */
                @Override
                public DateFormat getDateFormat(Locale locale) {
                    if (locale == null) {
                        locale = Locale.getDefault();
                    }
                    return new SimpleDateFormat("dd-MM-yyyy", locale);
                }
            };
        } else return super.getConverter(type);
    }
}
