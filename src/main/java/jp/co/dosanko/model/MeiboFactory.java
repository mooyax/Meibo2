/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.dosanko.model;

import java.util.List;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.query.SelectQuery;

/**
 *
 * @author igahito
 */
public class MeiboFactory {
    static List<Meibo> getBeanCollection(){
        DataContext context=DataContext.createDataContext();
        SelectQuery query=new SelectQuery(Meibo.class);
        List<Meibo> result=context.performQuery(query);
        return result;
    }
    
}
