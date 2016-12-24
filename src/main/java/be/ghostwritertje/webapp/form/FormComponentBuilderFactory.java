package be.ghostwritertje.webapp.form;

/**
 * Created by Jorandeboever
 * Date: 23-Dec-16.
 */
public class FormComponentBuilderFactory {
    public static TextFieldComponentBuilder textField(){
        return new TextFieldComponentBuilder();
    }

    public static PasswordTextFieldComponentBuilder password(){
        return new PasswordTextFieldComponentBuilder();
    }
}
