/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jp.co.dosanko.panels;


import org.apache.wicket.markup.html.WebMarkupContainer;
import com.wiquery.plugins.jqgrid.component.Grid;
import com.wiquery.plugins.jqgrid.component.event.OnPagingAjaxEvent;
import com.wiquery.plugins.jqgrid.component.event.OnSelectRowAjaxEvent;
import com.wiquery.plugins.jqgrid.component.event.OnSortColAjaxEvent;
import com.wiquery.plugins.jqgrid.model.GridColumnModel;
import com.wiquery.plugins.jqgrid.model.GridModel;
import com.wiquery.plugins.jqgrid.model.GridModel.HorizontalPosition;
import com.wiquery.plugins.jqgrid.model.IColumn;
import com.wiquery.plugins.jqgrid.model.SortOrder;
import jp.co.dosanko.model.Meibo;
import jp.co.dosanko.model.SearchSession;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsUtils;
import org.odlabs.wiquery.ui.themes.ThemeUiHelper;

/**
 *
 * @author igahito
 */
public final class ResultPanel extends Panel {

    private static final long serialVersionUID =1L;

    WebMarkupContainer context;
    GridModel<Meibo> model;

    @Override
    protected void onDetach() {
        //model.detach();
        super.onDetach();
        
    }
    
    
    public ResultPanel(String id,IDataProvider<Meibo> provider,boolean isSortable){
        super(id);
        
        if(provider==null){
            provider=SearchSession.get().getDataProvider();
        }
        
/*
        anchor=new WebMarkupContainer("anchor");
        
        anchor.add(new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {

            @Override
            public JsScope callback() {
                System.out.println("reloadGrid");
                //return JsScope.quickScope("jQuery(\"#"+ResultPanel.this.get("context:gridtable:grid").getMarkupId()+"\").setGridParam({page:1,sortname:'bunrui',sortorder: 'asc'}).trigger(\"reloadGrid\");");
                return ResultPanel.this.reloadGrid();
            }
        }));   
        
        
      

            

 
         

        anchor.setOutputMarkupId(true);
        anchor.setOutputMarkupPlaceholderTag(true);
        if(isSortable){
            this.anchor.setVisible(true);
        }else{
            this.anchor.setVisible(false);
        }
        add(anchor);
        
    */
        
        
        WebMarkupContainer anchor = new AjaxFallbackLink("anchor"){

            @Override
            public void onClick(AjaxRequestTarget target) {
                model.setSortOrder(SortOrder.asc);
                //target.appendJavascript("$(\"#"+ResultPanel.this.get("context:gridtable:grid").getMarkupId()+"\").setGridParam({page:1,sortname:'bunrui',sortorder: 'asc'}).trigger(\"reloadGrid\");");
                target.appendJavascript((String)new JsQuery(ResultPanel.this.get("context:gridtable:grid")).$().chain("setGridParam","{page:1,sortname:'bunrui',sortorder: 'asc'}").chain("trigger",JsUtils.quotes("reloadGrid")).render());
            }
         
        };
        ThemeUiHelper.buttonRounded(anchor); 
       /*
        Label anchor=new Label("anchor","並び順を戻す");
        anchor.setOutputMarkupId(true);
        anchor.setOutputMarkupPlaceholderTag(true);
        anchor.add(new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public JsScope callback() {
                return JsScope.quickScope(new JsQuery(ResultPanel.this.get("context:gridtable:grid")).$().chain("setGridParam","{page:1,sortname:'bunrui',sortorder: 'asc'}").chain("trigger",JsUtils.quotes("reloadGrid")).render());
            }
            
        }));
        * 
        */
        add(anchor);
        
        //SelectQuery query=SearchSession.get().getQuery();


        
        

        model=new GridModel(){

            @Override
            public IColumn getInitialSort() {
                return null;
            }
            
        };

        //model.setObject("Meibo");
        model.setPagerpos(HorizontalPosition.left);
        model.setCaption((String)null);
        
        model.setSortOrder(SortOrder.asc);

        
        try {         
            for(String key:Meibo.getPosFields()){
                String val=Meibo.getFields().get(key);
                //System.out.println(val+":"+key);
                GridColumnModel<Meibo> column=new GridColumnModel<Meibo>(val, val, new Model<String>(key), 55);
                if(isSortable){
                    column.setSortable(true);
                }else{
                    column.setSortable(false);
                }
                model.addColumnModel(column);
            }

        } catch (Exception ex) {
            System.out.println("model.addColumnModel error"+ex);
        }

        model.setHeight("500px");
        model.setAutowidth(false);
        model.setShrinkToFit(true);
        model.setAlternateRows(true);

        model.setRowNum(-1);
        model.setRowList(null);
        
        //model.setScroll(2000);      
        
        context = new WebMarkupContainer("context");
        context.setOutputMarkupId(true);
        add(context);


        Grid<Meibo> grid = new Grid<Meibo>("gridtable", model, provider);
 

          
        grid.addEvent(new OnSortColAjaxEvent<Meibo>(){

            @Override
            protected void onSortCol(AjaxRequestTarget target, Grid<Meibo> grid, IColumn<Meibo> column, int col, String sortProperty, SortOrder order) {
                    //System.out.println("onSortCol was clicked!");             
            }


        });

        
        grid.addEvent(new OnPagingAjaxEvent<Meibo>() {
			@Override
			protected void onPaging(AjaxRequestTarget target,Grid<Meibo> grid,PageButton button) {
				//System.out.println(button.name() + " was clicked!");
			}

        });
        
        grid.addEvent(new OnSelectRowAjaxEvent<Meibo>(){

            @Override
            protected void onSelectRow(AjaxRequestTarget target, int row, IModel<Meibo> rowModel, boolean status) {
                //System.out.println("onSelectRow");
            }
            
        });
        
        context.add(grid);


    }

       
    


}
