/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.databinder.models.cay;


import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.util.SingleSortState;



/**
 *
 * @author igahito
 */
public class SortableCayenneProvider<T> extends CayenneProvider<T> implements ISortableDataProvider<T>{

    private static final long serialVersionUID =1L;
    //private ISortStateLocator sortStateLocator = null;
    private SingleSortState sortState = new SingleSortState();

    public SortableCayenneProvider() {
        super();
    }



    @Override
    public ISortState getSortState() {
        return sortState;
    }

 


    
    @Override
    public void setSortState(ISortState state) {
//        System.out.println("SortableCayenneProvider setSortState!!"+((SingleSortState)state).getSort());

        
        this.sortState = (SingleSortState)state;

        this.getQuery().clearOrderings();
        
        if(this.sortState!=null){
            String orderName=this.sortState.getSort().getProperty();
            if(!"".equals(orderName))
                this.getQuery().addOrdering(orderName,sortState.getSort().isAscending());
        }


        
        
    }







}
