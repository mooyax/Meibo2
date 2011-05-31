/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jp.co.dosanko.model;

import java.util.List;
import jp.co.dosanko.WicketApplication;
import org.apache.cayenne.query.SelectQuery;
import org.apache.wicket.Request;
import org.apache.wicket.Session;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.markup.repeater.data.IDataProvider;





/**
 *
 * @author igahito
 */
public class SearchSession extends AuthenticatedWebSession{

    private static final long serialVersionUID =1L;
    
    private Roles roles;
    private SelectQuery query;
    private SelectQuery rolesquery;
    private MeiboProvider<Meibo> dataProvider;
    private IDataProvider<Roles> roleProvider;
    private List<Meibo> tempList;
    private UserRoles userRoles;
//    private String freeword;
    
/*  
    private Map<String, List<String>> checkboxModel = null;

 
   public Map<String, List<String>> getCheckboxModel() {
        if (checkboxModel == null) {
            checkboxModel = new HashMap<String, List<String>>();
        }
        return checkboxModel;
    }

    public void setCheckboxModel(Map<String, List<String>> checkboxModel) {
        this.checkboxModel = checkboxModel;
        dirty();
    }
  */ 
/*    
    public String getFreeword() {
        return freeword;
    }

    public void setFreeword(String freeword) {
        this.freeword = freeword;
        dirty();
    }
*/
    public List<Meibo> getTempList() {
        return tempList;
    }

    public void setTempList(List<Meibo> tempList) {
        this.tempList = tempList;
        dirty();
    }

    
    public void setDataProvider(IDataProvider<Meibo> provider){
        this.dataProvider=(MeiboProvider<Meibo>) provider;
    }
    public IDataProvider<Meibo> getDataProvider() {
            if(dataProvider==null){
                dataProvider=new MeiboProvider<Meibo>();
                dataProvider.setQuery(query);
            }    
       
                return dataProvider;
            
    }

    public SearchSession(Request request) {
        super(request);

        query=new SelectQuery();
        
        dataProvider =new MeiboProvider<Meibo>();
        dataProvider.setQuery(query);


    }

    public SelectQuery getQuery() {
        return query;
    }

    public void setQuery(SelectQuery query) {
        this.query = query;
        dataProvider.setQuery(this.query);
        dirty();
    }



    @Override
    public boolean authenticate(String username, String password) {

        WicketApplication app=(WicketApplication) this.getApplication();
        try{
            UserRoles user=app.getRoleObject(username);
            if(user.getPassword().equals(password)){
                roles = new Roles(user.getRole());
                userRoles=user;
                return true;
            }else{
                roles = null;
                return false;
            }
        }catch(Exception e){
                roles = null;
                return false;
        }

    }

    @Override
    public Roles getRoles() {
        return roles;
    }

    public static SearchSession get(){
        return (SearchSession)Session.get();
    }


    public UserRoles getUserRoles(){
        return userRoles;
    }


    @Override
    public void invalidate() {
        super.invalidate();
        System.out.println("Session invalidate!!");
    }

    @Override
    protected void detach() {
     /*   if(checkboxModel!=null){
            this.checkboxModel.clear();
            this.checkboxModel=null;
        }
       */ 
        
        super.detach();
    }
    
    
    
    
    
    


}
