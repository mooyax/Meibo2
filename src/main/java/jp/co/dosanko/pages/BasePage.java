/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jp.co.dosanko.pages;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

/**
 *
 * @author igahito
 */
public class BasePage extends WebPage {
    private static final long serialVersionUID =1L;
    public BasePage() {
        super ();
        FeedbackPanel fb=new FeedbackPanel("feedback");
        fb.setOutputMarkupId(true);
        add(fb);
    }

    public BasePage(PageParameters params) {
        //書き換え
    }
}

