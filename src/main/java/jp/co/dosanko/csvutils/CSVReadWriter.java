/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.dosanko.csvutils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.cayenne.CayenneDataObject;
import org.apache.cayenne.access.DataContext;
import org.apache.wicket.util.resource.StringBufferResourceStream;

/**
 *
 * @author igahito
 */
public class CSVReadWriter<T extends CayenneDataObject> implements Serializable {

    private static final long serialVersionUID =1L;
    
    private static final String DELIMITER = ",";
    private String encode;
    private Class<T> base;
    private DataContext ctx;

    public CSVReadWriter(DataContext ctx, Class<T> base) {
        this.encode = System.getProperty("file.encoding");
        this.base = base;
        this.ctx = ctx;
    }

    public String getEncode() {
        return encode;
    }

    public CSVReadWriter<T> setEncode(String encode) {
        this.encode = encode;
        return this;
    }

    public List<T> read(File f, boolean registOn) throws FileNotFoundException, IOException {
        InputStreamReader in = new InputStreamReader(new FileInputStream(f), this.encode);
        BufferedReader reader = new BufferedReader(in, 10);

        List<T> entities = new ArrayList<T>();
        Map<String, String> map = new HashMap<String, String>();

        try {
            for (Field fs : base.getFields()) { //継承元も対象
                CSVAnnotation csv = fs.getAnnotation(CSVAnnotation.class);
                if (null != csv) {
                    map.put(csv.name(), fs.getName());
                }
            }

            String buffer;
            buffer = reader.readLine();
            String[] fieldNames = buffer.split(DELIMITER);

            int dataNo=0;
            while ((buffer = reader.readLine()) != null) {
                String[] values = buffer.split(DELIMITER);
                int i = 0;
                Object entity = null;
                if (registOn) {
                    entity = ctx.createAndRegisterNewObject(base);
                } else {
                    entity = base.newInstance();
                }
                for (String fieldName : fieldNames) {

                    try{
                        String field = map.get(fieldName).split("_")[0].toLowerCase();
                        this.setProperty(entity, field, values[i], base.getField(map.get(fieldName)).getType());
                    }catch(Exception e){
                        
                    }
                    i++;

                }
                editEntity((T)entity,dataNo);
                entities.add((T) entity);
                dataNo++;

            }


        } catch (Exception e) {
            System.out.println("convert error\n" + e.toString());
        }
        map=null;
        return entities;
    }

    public List<T> read(File f) throws FileNotFoundException, IOException {

        return read(f, true);
    }

    public void write(List<T> list, StringBufferResourceStream output) throws IOException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {

        //BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, this.encode));
        output.setCharset( Charset.forName(this.encode));
        final String separator = System.getProperty( "line.separator");

        Map<String, String> fieldMap = new HashMap<String, String>();
        List<String> pos = new ArrayList<String>();

        for (Field fs : base.getFields()) { //継承元も対象
            CSVAnnotation csv = fs.getAnnotation(CSVAnnotation.class);
            if (null != csv) {
                fieldMap.put(csv.name(), fs.getName());
                if (csv.priority() != Integer.MAX_VALUE) {
                    pos.add(csv.priority(), csv.name());
                }
            }
        }


        output.append(pos.get(0));
        for (int i = 1; i < pos.size(); i++) {
            output.append(",");
            output.append(pos.get(i));
        }
        output.append(separator);

        for (T o : list) {
            String fieldName = (String) base.getField(fieldMap.get(pos.get(0))).get(null);
            //output.append(this.readProp(o, fieldName));

            output.append((String)this.getProperty(o, fieldName));

            for (int i = 1; i < pos.size(); i++) {
                fieldName = (String) base.getField(fieldMap.get(pos.get(i))).get(null);
                output.append(",");
                try{
                    output.append((String)this.getProperty(o, fieldName));
                }
                catch(Exception e){
                      output.append("xxxx");
                    }
            }
            
            output.append(separator);

        }
        


    }
    
    Object getProperty(Object o,String name){
        Object result=null;
        try {
                String methodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
                Method m = base.getMethod(methodName);
                result=m.invoke(o);
                if(result==null)
                    result="";
        } catch (Exception e) {
        }
        return result;
    }
    
    void setProperty(Object o,String name,Object value,Class type){
         String methodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
         try{
            Method m = base.getMethod(methodName, type);
            m.invoke(o, value);
         }catch(Exception e){
         }
    }

    public void editEntity(T entity, int i) {
        //エンティティの操作
    }
}
