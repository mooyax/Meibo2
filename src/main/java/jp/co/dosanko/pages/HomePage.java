package jp.co.dosanko.pages;


import java.lang.String;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jp.co.dosanko.model.Meibo;
import jp.co.dosanko.model.MeiboProvider;
import jp.co.dosanko.model.SearchSession;
import jp.co.dosanko.panels.ResultPanel;
import jp.co.dosanko.panels.SearchInfoPanel;
import jp.co.dosanko.panels.SearchPanel;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;




/**
 * @author Hitoshi Igarashi -- HID igahito@dosanko.co.jp
 *
 */
public final class HomePage extends BasePage {

    private static final long serialVersionUID =1L;
    
    private IModel<Map<String, List<String>>> myModel;
    
    private IModel<String> freewordModel;
     

    
    public HomePage(final PageParameters parameters) {


 
        myModel= Model.ofMap(new HashMap<String, List<String>>());
        freewordModel=new Model<String>("");
        
        SearchInfoPanel searchInfo = new SearchInfoPanel("infoPanel",myModel,freewordModel);
        searchInfo.setOutputMarkupId(true);
        searchInfo.setOutputMarkupPlaceholderTag(true);
    	add(searchInfo);


        


    	final ResultPanel result = new ResultPanel("resultPanel",null,true);
        result.setOutputMarkupId(true);
        result.setOutputMarkupPlaceholderTag(true);
        add(result);



        final SearchPanel search = new SearchPanel("searchPanel",myModel,freewordModel);
        add(search);


        
        if(parameters.containsKey("result")){
            searchInfo.setVisible(true);
            result.setVisible(true);
        }else{
            searchInfo.setVisible(false);
            result.setVisible(false);

        }
        

        
    }

    @Override
    protected void onDetach() {
        myModel.detach();
        freewordModel.detach();
        super.onDetach();
    }


  

}
