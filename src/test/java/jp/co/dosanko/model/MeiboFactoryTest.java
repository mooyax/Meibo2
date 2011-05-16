/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.dosanko.model;

import java.util.List;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 *
 * @author igahito
 */
public class MeiboFactoryTest {
    
    public MeiboFactoryTest() {
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
    public void testGetBeanCollection() throws JRException {
        System.out.println("getBeanCollection");
        List expResult = null;
        List result = MeiboFactory.getBeanCollection();
   

        //JasperFillManager.fillReportToFile("/home/igahito/NetBeansProjects/meibo/meibo.jasper", null, new JRBeanCollectionDataSource(result));
        JasperPrint pdf = JasperFillManager.fillReport("/home/igahito/NetBeansProjects/meibo/src/main/webapp/reports/meibo.jasper", null, new JRBeanCollectionDataSource(result));
        JasperExportManager.exportReportToPdfFile(pdf,"/home/igahito/test.pdf");
    }
}
