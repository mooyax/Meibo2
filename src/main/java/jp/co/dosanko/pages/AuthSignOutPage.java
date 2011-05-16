/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jp.co.dosanko.pages;
import jp.co.dosanko.model.Meibo;
import jp.co.dosanko.model.SearchSession;
import org.apache.cayenne.query.SelectQuery;
import org.apache.wicket.PageParameters;
import org.apache.wicket.authentication.pages.SignOutPage;
import org.apache.wicket.markup.html.WebPage;

/**
 *
 * @author igahito
 */
public final class AuthSignOutPage extends SignOutPage {
    private static final long serialVersionUID =1L;
    public AuthSignOutPage() {
        super ();
        //SearchSession.get().setQuery(new SelectQuery(Meibo.class)); //セッションを破棄しているので、セットしてはいけない
    }
}

