package be.ghostwritertje.webapp;

import be.ghostwritertje.webapp.person.pages.LoginPage;
import com.googlecode.wickedcharts.wicket7.JavaScriptResourceRegistry;
import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.settings.BootstrapSettings;
import de.agilecoders.wicket.core.settings.IBootstrapSettings;
import de.agilecoders.wicket.themes.markup.html.bootswatch.BootswatchTheme;
import de.agilecoders.wicket.themes.markup.html.bootswatch.BootswatchThemeProvider;
import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;
import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.Session;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.component.IRequestableComponent;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

/**
 * Created by Ghostwritertje
 * Date: 29-Sep-16.
 */
public class MyApplication extends WebApplication {

    @Override
    public Class<? extends Page> getHomePage() {
        return DashboardPage.class;
    }

    @Override
    protected void init() {
        super.init();
        super.getComponentInstantiationListeners().add(new SpringComponentInjector(this));
        this.configureBootstrap();

        this.getSecuritySettings().setAuthorizationStrategy(new IAuthorizationStrategy.AllowAllAuthorizationStrategy() {
            public <T extends IRequestableComponent> boolean isInstantiationAuthorized(
                    Class<T> componentClass) {
                // Check if the new Page requires authentication (implements the marker interface)
                if (AuthorizationRequired.class.isAssignableFrom(componentClass) && !UnAuthorizedAllowed.class.isAssignableFrom(componentClass)) {
                    // Is user signed in?
                    if (CustomSession.get().isSignedIn()) {
                        // okay to proceed
                        return true;
                    }

                    // Intercept the request, but remember the target for later.
                    // Invoke Component.continueToOriginalDestination() after successful logon to
                    // continue with the target remembered.
                    throw new RestartResponseAtInterceptPageException(LoginPage.class);
                }

                return true;
            }
        });


        JavaScriptResourceRegistry.getInstance().setHighchartsReference(new WebjarsJavaScriptResourceReference("highcharts/current/highcharts.js"));

        JavaScriptResourceRegistry.getInstance().setFunnelReference(new WebjarsJavaScriptResourceReference("highcharts/current/modules/funnel.js"));

        JavaScriptResourceRegistry.getInstance().setHeatmapReference(new WebjarsJavaScriptResourceReference("highcharts/current/modules/heatmap.js"));

        JavaScriptResourceRegistry.getInstance().setHighchartsMoreReference(new WebjarsJavaScriptResourceReference("highcharts/current/highcharts-more.js"));

        JavaScriptResourceRegistry.getInstance().setHighchartsExportingReference(new WebjarsJavaScriptResourceReference("highcharts/current/modules/exporting.js"));
    }

    private void configureBootstrap() {
        IBootstrapSettings settings = new BootstrapSettings();
        settings.setThemeProvider(new BootswatchThemeProvider(BootswatchTheme.Paper));
        Bootstrap.install(this, settings);
    }

    @Override
    public Session newSession(Request request, Response response) {
        return new CustomSession(request);
    }
}
