/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.dosanko.panels;

import java.util.ArrayList;


import java.util.List;
import java.util.Map;



import jp.co.dosanko.model.Meibo;
import jp.co.dosanko.model.SearchSession;


import org.apache.cayenne.CayenneException;


import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.query.SelectQuery;
import org.apache.wicket.Component;


import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.markup.html.WebMarkupContainer;

import org.apache.wicket.markup.html.basic.Label;

import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.CheckGroupSelector;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.util.ListModel;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.javascript.JsUtils;

/**
 *
 * @author igahito
 */
public final class SearchPanel extends Panel {

    private static final long serialVersionUID = 1L;
    WebMarkupContainer debug;
    private IModel<String> freewordModel;

    public SearchPanel(String id, IModel<Map<String, List<String>>> myModel, IModel<String> freewordModel) {
        super(id, myModel);
        this.freewordModel = freewordModel;


        /*
        debug = new WebMarkupContainer("debug");
        
        debug.add(new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
        
        @Override
        public JsScope callback() {
        
        return JsScope.quickScope("var element=document.getElementById(\""+getPage().get("resultPanel:context:gridtable").getMarkupId()+"\"); var childs = element.childNodes;"
        //+"alert(\"sampleの2個目の子のIDは「\"+childs[1].id+\"」です\");");
        + "jQuery(\"#\"+childs[1].id.substring(5,10)).setGridParam({page:1,sortname:'bunrui',sortorder: 'asc'}).trigger(\"reloadGrid\");");
        }
        }));   
        
        
        
        
        
        debug.setOutputMarkupId(true);
        debug.setOutputMarkupPlaceholderTag(true);
        add(debug);
         */

        Form<Void> searchForm = new Form<Void>("searchForm");

        class SearchButton extends AjaxFallbackButton {

            public SearchButton(String id, Form<?> form) {
                super(id, form);

            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {


                String freeWord = SearchPanel.this.freewordModel.getObject();
                Expression exp = null;


                SelectQuery query = new SelectQuery(Meibo.class);
                //SelectQuery query = SearchSession.get().getQuery();
                query.setQualifier(null);
                query.addOrdering("bunrui", true);
                query.addOrdering("junjyo", true);

                if (freeWord != null) {
                    for (String name : Meibo.getFreewordList()) {
                        exp = ExpressionFactory.likeIgnoreCaseExp(name, "%" + freeWord + "%");
                        query.orQualifier(exp);
                    }

                }


                for (String name : Meibo.getSearchCheckList()) {
                    //List<String> model = SearchSession.get().getCheckboxModel().get(name);
                    //List<String> model = myModel.getObject().get(name);
                    List<String> model = ((Map<String, List<String>>) SearchPanel.this.getDefaultModelObject()).get(name);
                    if (model != null && !model.isEmpty()) {
                        if (name.equals("campus")) {
                            List<String> newModel = new ArrayList<String>();
                            for (String s : model) {
                                newModel.add((String.valueOf(Meibo.getCampusList().indexOf(s))));
                            }
                            exp = ExpressionFactory.inExp(name, newModel);

                        } else {
                            exp = ExpressionFactory.inExp(name, model);
                        }
                        query.andQualifier(exp);
                    }

                }


                SearchSession.get().setQuery(query);

                //((SortableCayenneProvider)SearchSession.get().getDataProvider()).setQuery(query);


                if (target != null) {
                    Component info = this.getPage().get("infoPanel");
                    Component result = this.getPage().get("resultPanel");
                    Component anchor = this.getPage().get("resultPanel:anchor");
                    info.setVisible(true);
                    target.addComponent(info);
                    
                    if (!result.isVisible()) {
                        result.setVisible(true);
                        target.addComponent(result);
                        //SearchSession.get().isFirst=false;
                        
                        
                    }
//                   JavaScriptを記述する例                
//                    target.appendJavascript("var grid=$(\"#"+getPage().get("resultPanel:context:gridtable").getMarkupId()+" table.tbody:first\");"
//                            +"grid.setGridParam({page:1,sortname:'bunrui',sortorder: 'asc'}).trigger(\"reloadGrid\");"
//                            );
                    target.appendJavascript((String)new JsQuery(getPage().get("resultPanel:context:gridtable")).$("table.tbody:first").chain("setGridParam","{page:1,sortname:'bunrui',sortorder: 'asc'}").chain("trigger",JsUtils.quotes("reloadGrid")).render());
                    target.addComponent(this);
                    
                    
                
                    target.focusComponent(anchor);






                }
            }
        }

        AjaxFallbackButton fsearchButton = new SearchButton("fsearch", searchForm);
        AjaxFallbackButton searchButton = new SearchButton("search", searchForm);


        searchForm.add(fsearchButton);
        searchForm.add(searchButton);

        /*    
        fsearchButton.add(new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
        
        @Override
        public JsScope callback() {
        
        return JsScope.quickScope("var element=document.getElementById(\""+getPage().get("resultPanel:context:gridtable").getMarkupId()+"\"); var childs = element.childNodes;"
        //+"alert(\"sampleの2個目の子のIDは「\"+childs[1].id+\"」です\");");
        + "jQuery(\"#\"+childs[1].id.substring(5,10)).setGridParam({page:1,sortname:'bunrui',sortorder: 'asc'}).trigger(\"reloadGrid\");");
        }
        }));  
        searchButton.add(new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
        
        @Override
        public JsScope callback() {
        
        return JsScope.quickScope("var element=document.getElementById(\""+getPage().get("resultPanel:context:gridtable").getMarkupId()+"\"); var childs = element.childNodes;"
        //+"alert(\"sampleの2個目の子のIDは「\"+childs[1].id+\"」です\");");
        + "jQuery(\"#\"+childs[1].id.substring(5,10)).setGridParam({page:1,sortname:'bunrui',sortorder: 'asc'}).trigger(\"reloadGrid\");");
        }
        }));
         */
        searchForm.add(new TextField<String>("freeword", this.freewordModel));
        add(searchForm);

        ListView<String> itemList = new ListView<String>("itemlist", new LoadableDetachableModel<List<String>>() {

            @Override
            protected List<String> load() {
                return Meibo.getSearchCheckList();
            }
        }) {

            @Override
            protected void populateItem(ListItem<String> item) {
                final String key = item.getModelObject();

                final Map<String, List<String>> model = ((Map<String, List<String>>) SearchPanel.this.getDefaultModelObject());
                //item.add(SearchPanel.this.setCheckGroups(key));
                if(!model.containsKey(key)){
                    model.put(key, new ArrayList());
                }

                item.add(new CheckGroup("group", new ListModel<String>(model.get(key)) 
                    
                ) {

                    {
                        add(new Label("title", new LoadableDetachableModel<String>() {

                            @Override
                            protected String load() {
                                return Meibo.getReverseFields().get(key);
                            }
                        }));
                        add(new CheckGroupSelector("groupselector"));

                        add(new ListView<String>("list", new LoadableDetachableModel<List<String>>() {

                            @Override
                            protected List<String> load() {

                                return Meibo.getDistinctList(key);


                            }
                        }) {

                            @Override
                            protected void populateItem(ListItem<String> item) {
                                item.add(new Check<String>("check", item.getModel()));
                                item.add(new Label("value", item.getDefaultModelObjectAsString()));
                            }
                        ;
                    }
                                
                );

            }

     
        }
        );
                    
      

            }

            
                 
            
            
        };

        searchForm.add(itemList);


    }

    private Component setCheckGroups(final String fieldName) throws CayenneException {



        CheckGroup checks = new CheckGroup("group", new IModel<List<String>>() {

            Map<String, List<String>> model = ((Map<String, List<String>>) SearchPanel.this.getDefaultModelObject());

            @Override
            public List<String> getObject() {
                if (!model.containsKey(fieldName)) {
                    model.put(fieldName, new ArrayList());
                }
                return model.get(fieldName);
            }

            @Override
            public void setObject(List<String> object) {
                model.put(fieldName, object);
            }

            @Override
            public void detach() {
                if (model.containsKey(fieldName)) {
                    model.remove(fieldName);
                }
            }
        });

        checks.add(new Label("title", new LoadableDetachableModel<String>() {

            @Override
            protected String load() {
                return Meibo.getReverseFields().get(fieldName);
            }
        }));
        checks.add(new CheckGroupSelector("groupselector"));
        checks.add(new ListView<String>("list", new LoadableDetachableModel<List<String>>() {

            @Override
            protected List<String> load() {
                return Meibo.getDistinctList(fieldName);


            }
        }) {

            @Override
            protected void populateItem(ListItem<String> item) {
                item.add(new Check<String>("check", item.getModel()));
                item.add(new Label("value", item.getDefaultModelObjectAsString()));
            }
        ;
        });

        return checks;
    }

    @Override
    protected void onDetach() {
        this.freewordModel.detach();
        super.onDetach();
    }
}
