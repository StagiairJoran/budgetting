package be.ghostwritertje.webapp.form;

import java.io.Serializable;

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

    public static <X extends Number & Comparable<X>> NumberTextFieldComponentBuilder<X> number(Class<X> xClass){
        return new NumberTextFieldComponentBuilder();
    }

    public static LocalDateTextFieldComponentBuilder date(){
        return new LocalDateTextFieldComponentBuilder();
    }

    public static <X extends Serializable & Display> DropDownComponentBuilder<X> dropDown(){
        return new DropDownComponentBuilder<>();
    }
}
