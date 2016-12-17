import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

/**
 * Created by Ghostwritertje
 * Date: 29-Sep-16.
 */
public class MyApplication extends WebApplication {

    @Override
    public Class<? extends Page> getHomePage() {
        return Hello.class;
    }

    @Override
    protected void init() {
        super.init();
        super.getComponentInstantiationListeners().add(new SpringComponentInjector(this));
    }
}
