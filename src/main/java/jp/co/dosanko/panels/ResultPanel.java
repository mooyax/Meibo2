/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jp.co.dosanko.panels;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.odlabs.wiquery.core.events.Event;
import org.apache.wicket.markup.html.WebMarkupContainer;
import com.wiquery.plugins.jqgrid.component.Grid;
import com.wiquery.plugins.jqgrid.component.event.OnPagingAjaxEvent;
import com.wiquery.plugins.jqgrid.component.event.OnSortColAjaxEvent;
import com.wiquery.plugins.jqgrid.model.GridColumnModel;
import com.wiquery.plugins.jqgrid.model.GridModel;
import com.wiquery.plugins.jqgrid.model.GridModel.HorizontalPosition;
import com.wiquery.plugins.jqgrid.model.IColumn;
import com.wiquery.plugins.jqgrid.model.SortOrder;
import java.util.List;
import java.util.Map;
import jp.co.dosanko.model.Meibo;
import jp.co.dosanko.model.MeiboProvider;
import jp.co.dosanko.model.SearchSession;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.util.SingleSortState;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.Model;
import org.odlabs.wiquery.core.events.MouseEvent;
import static org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState.*;
import org.apache.wicket.model.IModel;
import org.odlabs.wiquery.core.events.WiQueryEventBehavior;
import org.odlabs.wiquery.core.javascript.JsScope;

/**
 *
 * @author igahito
 */
public final class ResultPanel extends Panel {

    private static final long serialVersionUID =1L;

    WebMarkupContainer context;
    WebMarkupContainer anchor;

    @Override
    protected void onDetach() {
        //model.detach();
        super.onDetach();
        
    }

    public JsScope reloadGrid(){
        return JsScope.quickScope("jQuery(\"#"+ResultPanel.this.get("context:gridtable:grid").getMarkupId()+"\").setGridParam({page:1,sortname:'bunrui',sortorder: 'asc'}).trigger(\"reloadGrid\");");
    }
    
    
    public ResultPanel(String id,IDataProvider<Meibo> provider,boolean isSortable){
        super(id);
        

        anchor=new WebMarkupContainer("anchor");
        anchor.add(new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {

            @Override
            public JsScope callback() {
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
        
        
        
        //SelectQuery query=SearchSession.get().getQuery();

        if(provider==null){
            provider=SearchSession.get().getDataProvider();
        }
        
        

        GridModel<Meibo> model=new GridModel(Meibo.class){

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

        model.setSortOrder(SortOrder.asc);
        
        context = new WebMarkupContainer("context");
        context.setOutputMarkupId(true);
        add(context);


        Grid<Meibo> grid = new Grid<Meibo>("gridtable", model, provider);
 
        
 /*       grid.addEvent(new OnSortColAjaxEvent<Meibo>(){

            @Override
            protected void onSortCol(AjaxRequestTarget target, Grid<Meibo> grid, IColumn<Meibo> column, int col, String sortProperty, SortOrder order) {
  

                
               
                MeiboProvider provider=((MeiboProvider)SearchSession.get().getDataProvider());

                SingleSortState sortState=new SingleSortState();
                sortState.setPropertySortOrder(column.getPropertyPath(), order.equals(SortOrder.asc)?ASCENDING:DESCENDING);
                provider.setSortState(sortState);
                provider.setQuery(provider.getQuery());
                System.out.println("onSortCol");

      
               
            }


        });
*/
        
 /*       grid.addEvent(new OnPagingAjaxEvent<Meibo>() {


			@Override
			protected void onPaging(
					AjaxRequestTarget target,
					Grid<Meibo> grid,
					PageButton button) {
				//System.out.println(button.name() + " was clicked!");
			}


        });
*/

        context.add(grid);


    }

       
    


}
