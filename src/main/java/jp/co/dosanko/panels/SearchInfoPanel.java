/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jp.co.dosanko.panels;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import jp.co.dosanko.csvutils.MeiboCSVReadWriter;
import jp.co.dosanko.jasper.JasperReportsResource;

import jp.co.dosanko.model.Meibo;
import jp.co.dosanko.model.MeiboProvider;
import jp.co.dosanko.model.SearchSession;
import net.databinder.models.cay.CayenneProvider;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Response;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.IStringResourceStream;
import org.apache.wicket.util.resource.StringBufferResourceStream;



/**
 *
 * @author igahito
 */
public class SearchInfoPanel extends Panel {
    
    private static final long serialVersionUID =1L;
    private IModel<String> freewordModel;
 
    
    public SearchInfoPanel(String id,IModel<Map<String, List<String>>> myModel,IModel<String> freewordModel) {
        super (id,myModel);
        this.freewordModel=freewordModel;

 
        add(new Label("freeword",new Model(){

            @Override
            public String getObject() {
                return SearchInfoPanel.this.freewordModel.getObject();
            }

        }));
        
        
        add(new ListView<String>("searchinfo",new LoadableDetachableModel<List<String>>(){

            @Override
            protected List<String> load() {
                return Meibo.getSearchCheckList();
            }
                }) {
            @Override
            protected void populateItem(ListItem<String> item) {
                final String st=item.getModelObject();
                item.add(new Label("fieldName",Meibo.getReverseFields().get(st)));
                item.add(new Label("value",new AbstractReadOnlyModel(){

                    @Override
                    public String getObject() {
                        String val;
                        try{
                            //val=SearchSession.get().getCheckboxModel().get(st).toString();
                          
                            val=((Map<String, List<String>>)SearchInfoPanel.this.getDefaultModelObject()).get(st).toString();
                            val=val.substring(1,val.length()-1);
                        }catch(Exception e){
                            val="";
                        }
                        return val;
                    }

                }));

            }
        });

        Label total=new Label("total",new Model<String>(){

            @Override
            public String getObject(){
                return String.valueOf(SearchSession.get().getDataProvider().size());
            }

        });

        add(total);
        
        final Form downloadForm = new Form("downloadform");
        add(downloadForm);
        downloadForm.add(new SubmitLink("download", downloadForm) {

            @Override
            public void onSubmit() {

                final StringBufferResourceStream resource = new StringBufferResourceStream("application/octet-stream");

                try {
         
                    MeiboCSVReadWriter<Meibo> reader=(MeiboCSVReadWriter<Meibo>) new MeiboCSVReadWriter<Meibo>(null, Meibo.class).setEncode("MS932");
                    reader.write(((CayenneProvider)SearchSession.get().getDataProvider()).getList(), resource);
                } catch (Exception e) {
                }
                getRequestCycle().setRequestTarget(new ResourceStreamRequestTarget(resource) {

                    @Override
                    protected void configure(RequestCycle requestCycle, Response response, IResourceStream resourceStream) {
                        super.configure(requestCycle, response, resourceStream);
                        final IStringResourceStream stream = (IStringResourceStream) resourceStream;
                        final WebResponse webResponse = (WebResponse) response;
                        webResponse.setAttachmentHeader("download.csv");
                        try {
                            System.out.println("stream.asString().getBytes(MS932).length=" + stream.asString().getBytes("MS932").length);
                            //System.out.println(stream.asString());
                            webResponse.setContentLength(stream.asString().getBytes("MS932").length);
                        } catch (UnsupportedEncodingException ex) {
                            error("No such encoding: " + "MS932");
                        }
                    }
                });
    
            }
        });
        
        add(new ResourceLink("exportPDF", new JasperReportsResource(){

            @Override
            public JasperPrint getJasperPrint() {
                JasperPrint jasperPrint = null;

                System.out.println("path:"+WebApplication.get().getServletContext().getRealPath("/"));
        
                List result=((CayenneProvider)SearchSession.get().getDataProvider()).getList();
                try {
                    jasperPrint=JasperFillManager.fillReport(WebApplication.get().getServletContext().getRealPath("/reports/meibo.jasper"), null, new JRBeanCollectionDataSource(result));
                } catch (JRException ex) {
                    System.out.println("path:"+WebApplication.get().getServletContext());
                }
                return jasperPrint;
            }
            
        }.setFileName("meibo")));
        
    }

    @Override
    protected void onDetach() {
        this.freewordModel.detach();
        super.onDetach();
    }
    
    
    

}
            
