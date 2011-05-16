/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.databinder.models.cay;

import java.util.Iterator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SingleSortState;
import jp.co.dosanko.model.Meibo;
import org.apache.cayenne.query.SelectQuery;
import org.apache.cayenne.access.DataContext;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsNot.*;

/**
 *
 * @author igahito
 */
public class SortableCayenneProviderTest {
    
    DataContext context;
    SortableCayenneProvider<Meibo> instance;
    public SortableCayenneProviderTest() {

        context = DataContext.createDataContext();
        SelectQuery query=new SelectQuery(Meibo.class);
        instance = new SortableCayenneProvider<Meibo>();
        instance.setQuery(query);
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }



    
    /**
     * Test of setSortState method, of class SortableCayenneProvider.
     */
    @Test
    public void testSetSortState() {
        System.out.println("setSortState");
        SingleSortState state = new SingleSortState();
        state.setSort(new SortParam("name",true));
        instance.setSortState(state);
        String result=instance.getSortState().toString();
        assertThat(result,is("[SingleSortState sort=[SortParam property=name ascending=true]]"));
        
    }
    
    @Test
    public void testGetSetSortState(){
        System.out.println("getSortState");
                
        SingleSortState state = new SingleSortState();
        state.setSort(new SortParam("name",true));
        instance.setSortState(state);
        System.out.println(instance.getSortState().toString());
        state = new SingleSortState();
        state.setSort(new SortParam("yakushoku",true));
        instance.setSortState(state);
        System.out.println(instance.getSortState().toString());
    }
    
    @Test
    public void データ取得テスト(){
        context = DataContext.createDataContext();
        SelectQuery query=new SelectQuery(Meibo.class);
        instance = new SortableCayenneProvider<Meibo>();
        instance.setQuery(query);
        Iterator<Meibo> it=(Iterator<Meibo>) instance.iterator(0, instance.size());
        /*while(it.hasNext()){
            System.out.println(((Meibo)it.next()).getHname());
        }
         * 
         */
        int result=instance.size();
        assertThat(result,not(0));
    }
}
