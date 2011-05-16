/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.dosanko.model;

import java.util.List;
import net.databinder.models.cay.SortableCayenneProvider;
import org.apache.cayenne.query.SelectQuery;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.util.SingleSortState;


/**
 *
 * @author igahito
 */
public class MeiboProvider<T> extends SortableCayenneProvider<T>{

    private static final long serialVersionUID =1L;
    
    transient List<T> list; 
    
    transient String orderold;
    
    public MeiboProvider() {
        super();
    }

    @Override
    public void setQuery(SelectQuery query) {
        query.setPageSize(20);
        super.setQuery(query);
        list=super.getList();
        

    }

    @Override
    public List<T> getList() {
        if(list==null){
            list=super.getList();
        }
        return list;
    }
 
    
    @Override
    public void setSortState(ISortState state) {
        String oldstate=this.orderold;        
        super.setSortState(state);
        String newstate=((SingleSortState) this.getSortState()).getSort().toString();
        if(state!=null ){
            if(!newstate.equals(oldstate)){              
                this.getQuery().addOrdering("bunrui",true);
                this.getQuery().addOrdering("junjyo",true);
                this.setQuery(this.getQuery());  
                this.orderold=newstate;
                //System.out.println("setSortState(old)"+oldstate);
                //System.out.println("setSortState(new)"+newstate);
            }
            
            
        }
        
        

    }
    
    
}
