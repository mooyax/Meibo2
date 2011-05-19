/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jp.co.dosanko.pages;
import org.apache.wicket.authentication.pages.SignOutPage;


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

