package jp.co.dosanko.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import jp.co.dosanko.csvutils.CSVAnnotation;
import jp.co.dosanko.model.auto._Meibo;
import net.databinder.cay.Databinder;
import org.apache.cayenne.CayenneException;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.access.ResultIterator;
import org.apache.cayenne.query.SQLTemplate;


public class Meibo extends _Meibo implements Serializable{

  private static final long serialVersionUID =1L;
  
  private static final List<String> campusList=Arrays.asList("役員・名誉教授","札幌","函館","旭川","釧路","岩見沢");
  private static final List<String> searchCheckList = Arrays.asList("campus","kyoku","bukoushitsu","syozokuka","bgroup","senkou","bunya","kamoku","kyouikukouza");
  private static final List<String> freewordList = Arrays.asList("yakushoku", "name", "hname", "tel1", "tel2", "fax", "mail");
  private static Map<String,String> fieldsMap=null;
  private static Map<String,String> reverseFieldMap=null;
  private static List<String> posFields=null;

  private static Map<String, List<String>> distinctMap = Collections.synchronizedMap(new HashMap<String, List<String>>());

    public static Map<String, List<String>> getDistinctMap() {
        return distinctMap;
    }
    
    
  public static List<String> getFreewordList() {
        return freewordList;
  }
 
  public static List getCampusList(){
      return campusList;
  }

  public static void init(){
      initGetFields();
      initGetReverseFields();
      initGetPosFields();
      setDistinctList();
  }
  
  private static void initGetFields(){
   if(fieldsMap==null){
        Map<String,String> map=new HashMap<String,String>();
        for(Field fs :Meibo.class.getFields()){
                CSVAnnotation csv=fs.getAnnotation(CSVAnnotation.class);
                if (null != csv) {
                    try {
                        map.put(csv.name().intern(), (String)fs.get(null));
                    } catch (Exception ex) {
                    }
                }
        }
        fieldsMap=map;
        map=null;  
   }
  }
  
  private static void initGetReverseFields(){
      if(reverseFieldMap==null){     
        Map<String,String> map=new HashMap<String,String>();
        for(Field fs :Meibo.class.getFields()){
                CSVAnnotation csv=fs.getAnnotation(CSVAnnotation.class);
                if (null != csv) {
                    try{
                        map.put((String)fs.get(null),csv.name().intern());
                    }catch(Exception e){
                        
                    }
                }
        }
        System.out.println("************************getReverseFields init!!");
        reverseFieldMap=map;
        map=null;     
      }    
  }
  
  private static void initGetPosFields(){
      if(posFields==null){
        List<String> pos=new ArrayList<String>();
        for(Field fs :Meibo.class.getFields()){
                CSVAnnotation csv=fs.getAnnotation(CSVAnnotation.class);
                if (null != csv) {
                    if(csv.priority()!=Integer.MAX_VALUE && csv.priority()!=0){
                        pos.add(csv.priority()-1,csv.name().intern());
                    }

                }
        }
        posFields=pos;
        pos=null;
      }    
  }
  public static Map<String,String> getFields(){
      return fieldsMap;
  }

  public static Map<String,String> getReverseFields(){
      return reverseFieldMap;
  }

  public static List<String> getPosFields(){    
      return posFields;
  }
  


    @Override
    public String getCampus() {
        return (String) Meibo.getCampusList().get(Integer.parseInt(super.getCampus()));
    }

    @Override
    public void setCampus(String campus) {
        int pos=Meibo.getCampusList().indexOf(campus);
        super.setCampus(String.valueOf(pos));
    }
  


    
    
    public static List<String> getSearchCheckList() {
        return searchCheckList;
    }
    
    public static void setDistinctList(){   
            getDistinctMap().clear();
            for(String name:Meibo.searchCheckList){
                String key=name.toUpperCase();
                List<String> result = new ArrayList();
                ResultIterator objs = null;

                try {
                    SQLTemplate query = new SQLTemplate(Meibo.class, "select distinct " + key + " from APP.MEIBO order by " + key);
                    DataContext context;
                    try{
                        context = Databinder.getContext();
                    }catch(Exception e){
                        context = DataContext.createDataContext(); 
                    }
                    objs = context.performIteratedQuery(query);

                    while (objs.hasNextRow()) {

                        String s = (String) objs.nextDataRow().get(key);


                        if (s != null) {
                            if (!s.isEmpty()) {
                                s = s.intern();
                                result.add(s);
                            }
                        }
                    }

                } catch (CayenneException e) {
                    System.out.println(e);
                } finally {
                    try {
                        if (objs != null) {
                            objs.close();
                        }
                    } catch (CayenneException closeEx) {
                        System.out.println(closeEx);
                    }
                }

                if (name.equals("campus")) {
                    List<String> l = new ArrayList<String>();
                    for (String s : result) {
                        l.add((String) Meibo.getCampusList().get(Integer.valueOf(s)));
                    }
                    result = l;
                }

                System.out.println("setDistinctList "+name+":"+result.toString());
                getDistinctMap().put(name, result);
            }
    }
    public static List<String> getDistinctList(String name) {

        List<String> tmp=null;
        if(getDistinctMap().containsKey(name))
            tmp= new ArrayList(getDistinctMap().get(name));
        
        return tmp;
    }

}
