/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jp.co.dosanko.csvutils;

import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.wicket.util.resource.StringBufferResourceStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.junit.Ignore;
import org.apache.cayenne.ObjectContext;
import java.util.Iterator;
import org.apache.cayenne.query.SelectQuery;
import org.apache.cayenne.DataObject;


import org.apache.cayenne.access.DataContext;
import jp.co.dosanko.model.Meibo;
import java.io.File;
import java.util.List;
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
public class CSVReadWriterTest {

    CSVReadWriter<Meibo> instance;
    DataContext context;
    private int id;
    public CSVReadWriterTest() {
        context = DataContext.createDataContext();
        instance=new CSVReadWriter<Meibo>(context,Meibo.class);
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
     * Test of getEncode method, of class CSVReadWriter.
     */
    @Ignore
    @Test
    public void testGetEncode() {
        System.out.println("getEncode");
        String expResult = "";
        String result = instance.getEncode();
        assertThat(instance.getEncode(),is("UTF-8"));
    }

    /**
     * Test of setEncode method, of class CSVReadWriter.
     */
    @Test
    public void testSetEncode() {
        System.out.println("setEncode");
        String encode = "MS932";

        CSVReadWriter expResult = null;
        CSVReadWriter result = instance.setEncode(encode);
        assertThat(instance.getEncode(),is("MS932"));

    }

    /**
     * Test of read method, of class CSVReadWriter.
     */
    @Ignore
    @Test
    public void testRead() throws Exception {
        System.out.println("read");
        String encode = "MS932";
        instance.setEncode(encode);
        File f = new File("/home/igahito/meibocsv/sapporo.csv");
        List expResult = null;
        List<Meibo> result = instance.read(f);

        assertThat(result.size(),is(387));
        
        Iterator i = result.iterator();
        while(i.hasNext()) {
            Meibo m = (Meibo)i.next();
            System.out.println(m.getHname() + ":" + m.getName());
        }
    }

    @Ignore //DB書き込みするため
    @Test
    public void insertDBTest() throws Exception{
        

        //Meibo meibo=(Meibo)context.newObject(Meibo.class);

        String encode = "MS932";
        instance.setEncode(encode);
        File f = new File("/home/igahito/Downloads/sapporo.csv");
        List expResult = null;
        List<Meibo> result = instance.read(f);



        //PropertyUtils.copyProperties(meibo, result.get(0));

 
        context.commitChanges();

        SelectQuery query = new SelectQuery(Meibo.class);
        List list = context.performQuery(query);

        Iterator i = list.iterator();
        while(i.hasNext()) {
            Meibo m = (Meibo)i.next();
            System.out.println(m.getHname() + ":" + m.getName());
        }
 
    }

    @Test
    public void writeTest() throws UnsupportedEncodingException, FileNotFoundException, IOException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
        final StringBufferResourceStream resource = new StringBufferResourceStream("application/octet-stream");
        Expression qualifier = null;
        try {
            qualifier = ExpressionFactory.matchExp("campus","1");
        } catch (Exception e){
            fail("認証主とDBのフィールド不一致！");
        }
        SelectQuery query=new SelectQuery(Meibo.class,qualifier);
        query.addOrdering("junjyo",true);
        List<Meibo> list = context.performQuery(query);
                           
        instance.write(list, resource);
        
        System.out.println(resource.asString());



    }




}