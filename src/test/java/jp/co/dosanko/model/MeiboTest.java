/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.dosanko.model;


import org.apache.cayenne.access.ResultIterator;
import org.apache.cayenne.CayenneException;
import org.apache.cayenne.query.SQLTemplate;
import org.apache.cayenne.access.DataContext;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;

/**
 *
 * @author igahito
 */
public class MeiboTest {

    DataContext context;

    public MeiboTest() {
        try {
            context = DataContext.getThreadDataContext();
        } catch (Exception e) {
            context = DataContext.createDataContext();
        }
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

    /**
     * Test of getFields method, of class Meibo.
     */
    @Test
    public void testGetFields() {
        System.out.println("getFields");
        Meibo.init();
        Meibo instance = new Meibo();
        Map expResult = null;
        Map result = null;
        result = Meibo.getFields();
        

        for (Object key : result.keySet()) {
            System.out.println(key);
            System.out.println(((String) result.get(key)));
        }
        assertThat(result.size(), is(17)); //17項目


    }

    @Test
    public void rowDistinctTest() throws CayenneException {
        System.out.println("rowDistinctTest");
        //SelectQuery query=new SelectQuery(Meibo.class);
        ResultIterator objs = null;
        try {
            DataContext ctx = DataContext.createDataContext();
            SQLTemplate query = new SQLTemplate(Meibo.class, "select distinct CAMPUS from APP.MEIBO order by CAMPUS");
            objs = ctx.performIteratedQuery(query);


            while (objs.hasNextRow()) {
                System.out.println(objs.nextDataRow().get("CAMPUS"));

            }
        } catch (CayenneException e) {
            fail("失敗"+e);
        } finally {
            try {
                if (objs != null) {
                    objs.close();
                }
            } catch (CayenneException closeEx) {
                fail("失敗"+closeEx);
            }
        }



        /*
        SelectQuery query=new SelectQuery(Meibo.class);
        query.setDistinct(true);
        query.addOrdering("campus",true);
        List<Meibo> list=context.performQuery(query);
        
        for(Meibo m:list){
        System.out.println(m.getCampus());
        }
         */

    }

    @Test
    public void キャッシュQueryテスト() {
        System.out.println("キャッシュQueryテスト");

        class QueryThread extends Thread {

            public void run() {
                System.out.println("スレッドスタート");
                ResultIterator objs = null;
                try {
                    SQLTemplate query = new SQLTemplate(Meibo.class, "select distinct CAMPUS from APP.MEIBO order by CAMPUS");
                    query.setName("CAMPUS");
                    query.setCachePolicy(SQLTemplate.SHARED_CACHE);

                    System.out.println("1st time");
                    objs = context.performIteratedQuery(query);


                    while (objs.hasNextRow()) {
                        System.out.println(objs.nextDataRow().get("CAMPUS"));

                    }
                } catch (CayenneException e) {
                    fail("失敗"+e);
                } finally {
                    try {
                        if (objs != null) {
                            objs.close();
                        }
                    } catch (CayenneException closeEx) {
                        fail("失敗"+closeEx);
                    }
                }
            }
        }


        QueryThread thread1=new QueryThread();
        QueryThread thread2=new QueryThread();
        
        thread1.start();
        thread2.start();

    }
}