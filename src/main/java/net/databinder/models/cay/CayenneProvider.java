/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.databinder.models.cay;

import java.util.Iterator;
import java.util.List;
import net.databinder.cay.Databinder;
import net.databinder.models.PropertyDataProvider;
import org.apache.cayenne.DataObject;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.query.SelectQuery;
import org.apache.wicket.model.IModel;

/**
 *
 * @author igahito
 */
public class CayenneProvider<T> extends PropertyDataProvider<T>{

    private static final long serialVersionUID =1L;
    private SelectQuery query;
    //transient List<T> list=null;

    public SelectQuery getQuery() {
        return query;
    }
 
    public CayenneProvider() {
        super();
    }



    public void setQuery(SelectQuery query){
        this.query = query;
        /*DataContext context;
        try{
            context=Databinder.getContext();
        }catch(Exception e){
            context=DataContext.createDataContext();
        }          
        list=context.performQuery(query);
         */
        //this.query.setCachePolicy(GenericSelectQuery.LOCAL_CACHE);
        //this.query.setRefreshingObjects(false);
        //this.list=Databinder.getContext().performQuery(this.query);

    }



    


    public List<T> getList(){
        DataContext context;
        try{
            context=Databinder.getContext();
        }catch(Exception e){
            context=DataContext.createDataContext();
        }
        try{
            return context.performQuery(query);
        }catch(Exception e){
            return null;
        }
    }

    @Override
    protected IModel<T> dataModel(T object) {
        return new DataObjectModel((DataObject)object);
    }

    @Override
    public Iterator<? extends T> iterator(int first, int count) {
        //query.setPageSize(count);
        int toIndex = first + count;
	if (toIndex > this.size()){
		toIndex = this.size();
	}
        //System.out.println("first:"+first+","+"max:"+(toIndex)+","+"count:"+count);
        return this.getList().subList(first, toIndex).iterator();
    }

    @Override
    public int size() {
        return getList().size();
    }

    @Override
    public void detach() {
        
        
        /*if(list!=null){
            list.clear();
            list=null;
        }
         * 
         */
        query=null;
        super.detach();
    }
    
    
    
    

}
