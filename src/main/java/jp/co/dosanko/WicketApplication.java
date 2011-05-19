package jp.co.dosanko;

import java.lang.ref.WeakReference;

import javax.servlet.http.HttpServletRequest;
import jp.co.dosanko.model.Meibo;
import jp.co.dosanko.model.SearchSession;
import jp.co.dosanko.model.UserRoles;
import jp.co.dosanko.pages.AdminPage;
import jp.co.dosanko.pages.HomePage;
import jp.co.dosanko.pages.AuthSignInPage;
import jp.co.dosanko.pages.ErrorPage;
import jp.co.dosanko.pages.MyExpiredErrorPage;
import org.apache.wicket.Component;
import org.apache.wicket.Request;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.request.IRequestCycleProcessor;
import net.databinder.cay.DataApplication;
import net.databinder.models.cay.CayenneProvider;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.SelectQuery;
import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.authorization.IUnauthorizedComponentInstantiationListener;
import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.apache.wicket.authorization.strategies.role.IRoleCheckingStrategy;
import org.apache.wicket.authorization.strategies.role.RoleAuthorizationStrategy;
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadWebRequest;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.PageExpiredException;
import org.apache.wicket.protocol.http.WebRequestCycleProcessor;
import org.apache.wicket.util.file.Folder;
import org.apache.wicket.util.time.Duration;



/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 *
 * @see wicket.myproject.Start#main(String[])
 *
 * @author Hitoshi Igarashi - igahito@dosanko.co.jp
 *
 */
public final class WicketApplication extends DataApplication implements
        IRoleCheckingStrategy,
        IUnauthorizedComponentInstantiationListener {

    //private WiQueryInstantiationListener wiqueryPluginInstantiationListener;
    /** Subclass of authenticated web session to instantiate */
    private final WeakReference<Class<? extends AuthenticatedWebSession>> webSessionClassRef;
    private Folder uploadFolder = null; //一時的

    /**
     * Constructor
     */
    public WicketApplication() {
        // Get web session class to instantiate
        webSessionClassRef = new WeakReference<Class<? extends AuthenticatedWebSession>>(
                getWebSessionClass());
    }

    public Folder getUploadFolder() //一時的
    {

        return uploadFolder;
    }

    @Override
    protected void init() {
        super.init();
        //wiqueryPluginInstantiationListener = new WiQueryInstantiationListener();

        //addComponentInstantiationListener((IComponentInstantiationListener) wiqueryPluginInstantiationListener);

        this.mountBookmarkablePage("/index.html", HomePage.class);
        this.mountBookmarkablePage("/admin", AdminPage.class);

        this.getRequestCycleSettings().setResponseRequestEncoding("UTF-8");
        this.getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
        this.getRequestCycleSettings().setTimeout(Duration.minutes(10));
        
        //System.out.println("Session duration="+this.getRequestCycleSettings().getTimeout().toString());

        getSecuritySettings().setAuthorizationStrategy(new RoleAuthorizationStrategy(this));
        getSecuritySettings().setUnauthorizedComponentInstantiationListener(this);

        getResourceSettings().setThrowExceptionOnMissingResource(false); //一時的

        uploadFolder = new Folder(System.getProperty("java.io.tmpdir"), "wicket-uploads"); //一時的
        // Ensure folder exists
        uploadFolder.mkdirs(); //一時的

        //IApplicationSettings setting = this.getApplicationSettings();

        //setting.setPageExpiredErrorPage(HomePage.class);
        getApplicationSettings().setPageExpiredErrorPage(MyExpiredErrorPage.class);
        getApplicationSettings().setInternalErrorPage(MyExpiredErrorPage.class);
        Meibo.init();


    }

    /* (non-Javadoc)
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<HomePage> getHomePage() {
        return HomePage.class;
    }
/*
    @Override
    public String getConfigurationType() {
        return Application.DEPLOYMENT;
    }
*/
    @Override
    public Session newSession(Request request, Response response) {
        try {
            return webSessionClassRef.get().getDeclaredConstructor(Request.class).newInstance(
                    request);
        } catch (Exception e) {
            throw new WicketRuntimeException("Unable to instantiate web session "
                    + webSessionClassRef.get(), e);
        }
    }

    @Override
    public final boolean hasAnyRole(Roles roles) {
        final Roles sessionRoles = AuthenticatedWebSession.get().getRoles();
        return sessionRoles != null && sessionRoles.hasAnyRole(roles);
    }

    @Override
    public final void onUnauthorizedInstantiation(Component component) {
        // If there is a sign in page class declared, and the unauthorized
        // component is a page, but it's not the sign in page
        if (component instanceof Page) {
            if (!AuthenticatedWebSession.get().isSignedIn()) {
                // Redirect to intercept page to let the user sign in
                throw new RestartResponseAtInterceptPageException(getSignInPageClass());
            } else {
                onUnauthorizedPage((Page) component);
            }
        } else {
            // The component was not a page, so throw an exception
            throw new UnauthorizedInstantiationException(ErrorPage.class);
        }
    }

    /**
     * Called when an AUTHENTICATED user tries to navigate to a page that they are not authorized to
     * access. You might want to override this to navigate to some explanatory page or to the
     * application's home page.
     *
     * @param page
     *            The page
     */
    protected void onUnauthorizedPage(final Page page) {
        // The component was not a page, so throw an exception
        throw new UnauthorizedInstantiationException(HomePage.class);
    }

    /**
     * @return AuthenticatedWebSession subclass to use in this authenticated web application.
     */
    protected Class<? extends AuthenticatedWebSession> getWebSessionClass() {
        return SearchSession.class;
    }

    ;

    /**
     * @return Subclass of sign-in page
     */
    protected Class<? extends WebPage> getSignInPageClass() {
        return AuthSignInPage.class;
    }

    @Override
    protected WebRequest newWebRequest(HttpServletRequest servletRequest) {
        return new UploadWebRequest(servletRequest);
    }

    public UserRoles getRoleObject(String name) {
        Expression exp = Expression.fromString("name = '" + name + "'");
        SelectQuery rolesquery = new SelectQuery(UserRoles.class, exp);
        CayenneProvider roleProvider = new CayenneProvider<UserRoles>();
        roleProvider.setQuery(rolesquery);
        return (UserRoles) roleProvider.iterator(0, 1).next();
    }
    
/*  
    @Override
    protected IRequestCycleProcessor newRequestCycleProcessor() {
        return new WebRequestCycleProcessor() { 
              @Override 
            public void respond(RuntimeException e, RequestCycle requestCycle) 
            { 
                if (e instanceof PageExpiredException) { 
                    if(((WebRequest)RequestCycle.get().getRequest()).isAjax()) { 
                        AjaxRequestTarget rt = new AjaxRequestTarget(RequestCycle.get().getRequest().getPage()); 
                        //rt.appendJavascript("if(typeof window.handleAjaxSessionTimeout=='function')window.handleAjaxSessionTimeout()"); 
                        rt.appendJavascript("alert('セッションが切れました')");
                        
                        RequestCycle.get().setRequestTarget(rt); 
                        return; 
                    } 
                } 
                super.respond(e, requestCycle); 
            } 
        }; 
    }

  
    @Override
    public RequestCycle newRequestCycle(Request request, Response response) {
        return new CustomRequestCycle(this, (WebRequest) request, response);
    }

    public class CustomRequestCycle extends DataRequestCycle {

        public CustomRequestCycle(final WebApplication application, final WebRequest request, final Response response) {

            super(application, request, response);

        }

        @Override
        public final Page onRuntimeException(final Page cause, final RuntimeException e) {

            // obviously you can check the instance of the exception and return the appropriate page if desired

            if (e instanceof PageExpiredException) {

                if (this.getWebRequest().isAjax()) {

                    return new MyExpiredErrorPage(new PageParameters());

                }

            }
            
            if(e instanceof WicketRuntimeException){
                   return new MyExpiredErrorPage(new PageParameters());
            }

            return new HomePage(new PageParameters());

        }
    }
     * *
     */
}
