/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.dosanko.pages;

import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.authentication.panel.SignInPanel.SignInForm;
import org.apache.wicket.authentication.panel.SignInPanel;
import org.apache.wicket.markup.html.form.Form;

import jp.co.dosanko.panels.UpDownPanel;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import jp.co.dosanko.WicketApplication;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.After;

import org.junit.Before;
import org.junit.Ignore;

import org.junit.Test;


/**
 *
 * @author igahito
 */
public class AdminPageTest {
    
    private WicketTester tester;
	
    
    @Before
    public void setUp() {
        tester = new WicketTester(new WicketApplication());
        tester.startPage(AdminPage.class);
        
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void 認証ページ表示チェック(){
      tester.assertRenderedPage(AuthSignInPage.class);
    }
    

    @Test
    public void 認証チェック(){
      tester.assertRenderedPage(AuthSignInPage.class);
      tester.assertComponent("signInPanel",SignInPanel.class);
      tester.assertComponent("signInPanel:signInForm",SignInForm.class);
      signIn(); 
      tester.assertRenderedPage(AdminPage.class);
      
    }
    
    private void signIn(){
      FormTester formTester=tester.newFormTester("signInPanel:signInForm", true);
      formTester.setValue("username", "sapporo");
      formTester.setValue("password", "ssapporo");
      formTester.submit();
    }
    
    @Test
    public void HomePageコンポーネントチェック(){
        signIn(); 
        tester.assertComponent("feedback", FeedbackPanel.class); 
        tester.assertComponent("campus", Label.class);
        tester.assertComponent("link", Link.class); 
        tester.assertComponent("updownPanel", UpDownPanel.class); 
        tester.assertComponent("adminForm", Form.class); 
             
    }
    
    
    @Test
    public void 表示テスト(){
        signIn();
        tester.assertInvisible("resultPanel");
        tester.assertVisible("updownPanel");
    }
    
    
}
