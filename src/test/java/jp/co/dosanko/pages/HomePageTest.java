package jp.co.dosanko.pages;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import jp.co.dosanko.WicketApplication;
import jp.co.dosanko.model.SearchSession;

import jp.co.dosanko.panels.SearchPanel;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.util.tester.FormTester;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * @author Richard Wilkinson - richard.wilkinson@jweekend.com
 *
 */
public class HomePageTest {
	
	private WicketTester tester;
	
	@Before
	public void setUp()
	{
		tester = new WicketTester(new WicketApplication());
                tester.startPage(HomePage.class);
                tester.assertRenderedPage(HomePage.class);
	}

        @Test
        public void HomePageコンポーネントチェック(){
            tester.assertComponent("searchPanel", SearchPanel.class); 
            tester.assertComponent("searchPanel:searchForm", Form.class);
            tester.assertComponent("searchPanel:searchForm:fsearch", AjaxFallbackButton.class); 
            tester.assertComponent("searchPanel:searchForm:search", AjaxFallbackButton.class); 
             
        }
                
	@Test
	public void HomePageの表示テスト()
	{
                tester.assertInvisible("infoPanel");
                tester.assertInvisible("resultPanel");
                tester.assertVisible("searchPanel");
                
	}
        
        @Test
        public void HomePage全検索テスト()
        {
                
                FormTester formTester=tester.newFormTester("searchPanel:searchForm", true);
                tester.executeAjaxEvent("searchPanel:searchForm:fsearch", "onclick");
                       
                tester.assertVisible("infoPanel");
                tester.assertVisible("resultPanel");
                tester.assertVisible("searchPanel");
                
        }
        
        @Test
        public void HomePage札幌検索テスト()
        {
                
                FormTester formTester = tester.newFormTester("searchPanel:searchForm", true);
                formTester.setValue("itemlist:0:group", "check1"); 
                tester.executeAjaxEvent("searchPanel:searchForm:fsearch", "onclick");
                      
                tester.assertVisible("infoPanel");
                tester.assertVisible("resultPanel");
                tester.assertVisible("searchPanel");

                //札幌の検索結果 387
                SearchSession session = (SearchSession) tester.getWicketSession();
                assertThat(session.getDataProvider().size(),is(387));
                
        }

}
