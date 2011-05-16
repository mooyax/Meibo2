/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jp.co.dosanko.pages;

import jp.co.dosanko.model.Meibo;
import jp.co.dosanko.model.SearchSession;
import jp.co.dosanko.panels.ResultPanel;
import jp.co.dosanko.panels.UpDownPanel;
import net.databinder.cay.Databinder;

import org.apache.cayenne.CayenneException;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.ExpressionFactory;

import org.apache.cayenne.query.SelectQuery;


import org.apache.wicket.PageParameters;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;


import org.apache.wicket.markup.repeater.data.ListDataProvider;




/**
 *
 * @author igahito
 */

@AuthorizeInstantiation({Roles.ADMIN,Roles.USER})
public final class AdminPage extends WebPage {

    private static final long serialVersionUID =1L;

    //private static final Log log = LogFactory.getLog(AdminPage.class);
    private String campusName;

    ResultPanel result;
    UpDownPanel updown;
    

            

    
    public AdminPage(PageParameters params) {


        
        add(new FeedbackPanel("feedback"));
        campusName=SearchSession.get().getUserRoles().getDisplay();
        int id=SearchSession.get().getUserRoles().getId();
        Expression qualifier = null;
        try {
            qualifier = ExpressionFactory.matchExp("campus",String.valueOf(id));
        } catch (Exception e){
            this.error("認証主とDBのフィールド不一致！");
        }

        SelectQuery query=new SelectQuery(Meibo.class,qualifier);
        SearchSession.get().setQuery(query);
        Label label = new Label("campus",campusName);
        add(label);

        Link<String> link = new Link<String>("link"){
            

            @Override
            public void onClick() {
                this.setResponsePage(AuthSignOutPage.class);
            }
        };
        this.add(link);

        updown = new UpDownPanel("updownPanel");

        add(updown);

        result=null;
        if(params.isEmpty()){
            result = new ResultPanel("resultPanel",null,false);
            result.setVisible(false);
            updown.setVisible(true);
        }else{
            result = new ResultPanel("resultPanel",new ListDataProvider(SearchSession.get().getTempList()),false);
            result.setVisible(true);
            updown.setVisible(false);
        }
    	add(result);

        Button back = new Button("back"){

            @Override
            public void onSubmit() {
                setResponsePage(AdminPage.class);
            }

        };

        Button write = new Button("write"){

            @Override
            public void onSubmit() {
                try{
                    writeData();
                    //setResponsePage(AdminPage.class);
                    AdminPage.this.result.setVisible(false);
                    AdminPage.this.updown.setVisible(true);
                    Meibo.setDistinctList();
                    AdminPage.this.info("書き込みました！");
                }catch(Exception e){
                    AdminPage.this.error("書き込みに失敗しました！"+e.toString());
                }
                

            }

        };

        Form adminForm=new Form("adminForm");

        add(adminForm);

        adminForm.add(back);
        adminForm.add(write);



    }

    private void writeData() throws CayenneException{
        DataContext ctx = Databinder.getContext();

        //古いデータの削除
        SelectQuery query=SearchSession.get().getQuery();

        ctx.deleteObjects(ctx.performQuery(query));

        
        //新規データの登録
        for(Meibo m:SearchSession.get().getTempList()){
            ctx.registerNewObject(m);
        }
       
       
       ctx.commitChanges();


    }


}

