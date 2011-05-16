/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.dosanko.pages;


import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.pages.PageExpiredErrorPage;

/**
 *
 * @author mooya
 */
public final class MyExpiredErrorPage extends PageExpiredErrorPage {

    private static final long serialVersionUID =1L;
    
    public MyExpiredErrorPage(PageParameters params) {
        Link link=new Link("link") {

            @Override
            public void onClick() {
                getSession().invalidate();
		getRequestCycle().setRedirect(true);
		setResponsePage(HomePage.class);
            }
        };
        
        add(link);
    }
}
