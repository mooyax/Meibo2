/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.dosanko.csvutils;

import java.io.Serializable;

import org.apache.cayenne.CayenneDataObject;
import org.apache.cayenne.access.DataContext;
import jp.co.dosanko.model.Meibo;

/**
 *
 * @author igahito
 */
public class MeiboCSVReadWriter<T extends Meibo> extends CSVReadWriter implements Serializable{

    private static final long serialVersionUID =1L;
    
    public MeiboCSVReadWriter(DataContext ctx, Class base) {
        super(ctx, base);
    }

    @Override
    public void editEntity(CayenneDataObject entity, int i) {
        Meibo m=(Meibo)entity;
        int index=Meibo.getCampusList().indexOf(m.getCampus());
        //System.out.println("index="+index+" jyunjyo="+i);
        
        //System.out.println(String.format("%02d%06d", index,i));
        m.setJunjyo(String.format("%02d%06d", index,i));
    }




    
    
    
    
}
