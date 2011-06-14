/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.dosanko.jasper;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jp.co.dosanko.model.Meibo;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.query.SelectQuery;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author igahito
 */
public class JasperTest {
   

   static List<Meibo> getBeanCollection(){
            DataContext context=DataContext.createDataContext();
            SelectQuery query=new SelectQuery(Meibo.class);
            List<Meibo> result=context.performQuery(query);
            return result;
 
    
}
    public JasperTest() {
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
     * Test of getBeanCollection method, of class MeiboFactory.
     */
    @Test
    public void Jasperファイルの読み込みPDF出力テスト() {
        System.out.println("getBeanCollection");
        List expResult = null;
        List result = getBeanCollection();
   
        JasperPrint pdf;
        try {
            pdf = JasperFillManager.fillReport("/home/igahito/NetBeansProjects/meibo/src/main/webapp/reports/meibo.jasper", null, new JRBeanCollectionDataSource(result));
            JasperExportManager.exportReportToPdfFile(pdf,"/home/igahito/test.pdf");
        } catch (JRException ex) {
            fail("jasper file read or export error");
        }

    
        
    }
}
