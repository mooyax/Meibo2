/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.dosanko.panels;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;
import jp.co.dosanko.WicketApplication;
import jp.co.dosanko.csvutils.MeiboCSVReadWriter;
import jp.co.dosanko.model.Meibo;
import jp.co.dosanko.model.SearchSession;
import jp.co.dosanko.pages.AdminPage;
import net.databinder.models.cay.CayenneProvider;
import org.apache.cayenne.query.SelectQuery;
import org.apache.commons.io.FilenameUtils;
import org.apache.wicket.Application;
import org.apache.wicket.PageParameters;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Response;
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.file.Folder;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.IStringResourceStream;
import org.apache.wicket.util.resource.StringBufferResourceStream;

/**
 *
 * @author igahito
 */
public final class UpDownPanel extends Panel {

    private static final long serialVersionUID =1L;
    
    MeiboCSVReadWriter<Meibo> reader;

    
    public UpDownPanel(String id) {
        super(id);

 
        
        reader = (MeiboCSVReadWriter<Meibo>) new MeiboCSVReadWriter<Meibo>(null, Meibo.class).setEncode("MS932");

        /*
        Form form = new Form("downform") {
        @Override
        public void onSubmit() {
        IResourceStream stream = new AbstractResourceStreamWriter(){
        

        @Override
        public String getContentType() {
        return "application/octet-stream;charset=MS932";
        }




        @Override
        public void write(OutputStream output) {

        try {

        SortableCayenneProvider provider=(SortableCayenneProvider) SearchSession.get().getDataProvider();
        List<Meibo> dat = (List<Meibo>) provider.getList();
        System.out.println("table size is"+dat.size());
        for(Meibo m:dat){
        System.out.println(m.getHname());
        }
        reader.write(dat, output);
        output.flush();
        output.close();
        } catch (Exception e) {
        System.out.println("write error\n" + e.toString());
        }
        }
        };

        IRequestTarget target = new ResourceStreamRequestTarget(stream,"persons.csv");
        getRequestCycle().setRequestTarget(target);
        }

        };

        add(form);
         */

        final FileUploadForm ajaxSimpleUploadForm = new FileUploadForm("ajax-simpleUpload");

        final Form downloadForm = new Form("downloadform");
        add(downloadForm);
        downloadForm.add(new SubmitLink("download", downloadForm) {

            @Override
            public void onSubmit() {

                final StringBufferResourceStream resource = new StringBufferResourceStream("application/octet-stream");

                try {
                    CayenneProvider provider = (CayenneProvider) SearchSession.get().getDataProvider();
                    SelectQuery query=SearchSession.get().getQuery();
                    query.addOrdering("junjyo",true);
                    SearchSession.get().setQuery(query);
                    reader.write(provider.getList(), resource);
                } catch (Exception e) {
                }
                getRequestCycle().setRequestTarget(new ResourceStreamRequestTarget(resource) {

                    @Override
                    protected void configure(RequestCycle requestCycle, Response response, IResourceStream resourceStream) {
                        super.configure(requestCycle, response, resourceStream);
                        final IStringResourceStream stream = (IStringResourceStream) resourceStream;
                        final WebResponse webResponse = (WebResponse) response;
                        webResponse.setAttachmentHeader(SearchSession.get().getUserRoles().getName()+".csv");
                        try {
                            System.out.println("stream.asString().getBytes(MS932).length=" + stream.asString().getBytes("MS932").length);
                            //System.out.println(stream.asString());
                            webResponse.setContentLength(stream.asString().getBytes("MS932").length);
                        } catch (UnsupportedEncodingException ex) {
                            error("No such encoding: " + "MS932");
                        }
                    }
                });
                /*
                this.getRequestCycle().setRequestTarget(new ResourceStreamRequestTarget(new AbstractResourceStreamWriter() {



                @Override
                public String getContentType() {
                return "application/octet-stream;charset=MS932";
                }









                @Override
                public void write(OutputStream output) {

                try {
                CayenneProvider provider = (CayenneProvider) SearchSession.get().getDataProvider();
                reader.write(provider.getList(), output);
                } catch (Exception e) {

                e.printStackTrace();

                }finally{
                try {
                output.flush();
                output.close();
                } catch (IOException ex) {
                Logger.getLogger(UpDownPanel.class.getName()).log(Level.SEVERE, null, ex);
                }

                }
                }
                }, "person.csv"){

                @Override
                protected void configure(RequestCycle requestCycle, Response response, IResourceStream resourceStream) {
                super.configure(requestCycle, response, resourceStream);

                response.setContentLength(100000);
                System.out.println("response.setContentLength="+this.getResourceStream().length());
                }

                });

                 */
            }
        });

        ajaxSimpleUploadForm.add(new UploadProgressBar("progress", ajaxSimpleUploadForm));

        /*
        ajaxSimpleUploadForm.add( new Button("downloadButton"){

        @Override
        public void onSubmit() {
        IResourceStream stream = new AbstractResourceStreamWriter(){

        @Override
        public String getContentType() {
        return "application/octet-stream;charset=MS932";
        }


        @Override
        public void write(OutputStream output) {

        try {

        SortableCayenneProvider provider=(SortableCayenneProvider) SearchSession.get().getDataProvider();
        List<Meibo> dat = (List<Meibo>) provider.getList();
        System.out.println("table size is"+dat.size());
        for(Meibo m:dat){
        System.out.println(m.getHname());
        }
        reader.write(dat, output);

        } catch (Exception e) {
        System.out.println("write error\n" + e.toString());
        }
        }
        };

        IRequestTarget target = new ResourceStreamRequestTarget(stream,"persons.csv");
        getRequestCycle().setRequestTarget(target);
        }
        });
         */
        add(ajaxSimpleUploadForm);




    }

    private void checkFileExists(File newFile) {
        if (newFile.exists()) {
            // Try to delete the file
            if (!Files.remove(newFile)) {
                throw new IllegalStateException("Unable to overwrite " + newFile.getAbsolutePath());
            }
        }
    }

    private Folder getUploadFolder() {
        return ((WicketApplication) Application.get()).getUploadFolder();
    }

    /**
     * Form for uploads.
     */
    private class FileUploadForm extends Form<Void> {

        private FileUploadField fileUploadField;

        /**
         * Construct.
         *
         * @param name
         *            Component name
         */
        public FileUploadForm(String name) {
            super(name);

            // set this form to multipart mode (allways needed for uploads!)
            setMultiPart(true);

            // Add one file input field
            add(fileUploadField = new FileUploadField("fileInput"));

            // Set maximum size to 100K for demo purposes
            setMaxSize(Bytes.kilobytes(100));
        }

        /**
         * @see org.apache.wicket.markup.html.form.Form#onSubmit()
         */
        @Override
        protected void onSubmit() {
            final FileUpload upload = fileUploadField.getFileUpload();
            if (upload != null) {

                // Create a new file
                String fileName =FilenameUtils.getName(upload.getClientFileName());
                File newFile = new File(getUploadFolder(), fileName);

                // Check new file, delete if it already existed
                checkFileExists(newFile);
                String campusName=SearchSession.get().getUserRoles().getDisplay();
                try {
                    // Save to new file
                    newFile.createNewFile();
                    upload.writeTo(newFile);

                    List<Meibo> meibos = reader.read(newFile, false);
                    int id=SearchSession.get().getUserRoles().getId();
                    for (Meibo m : meibos) {
                        System.out.println(m.getHname());
                        if(!m.getCampus().equals(Meibo.getCampusList().get(id))){
                            //throw new IllegalStateException("管轄外のデータがあります！！");
                            //this.setResponsePage(AdminPage.class);
                            this.error("アップロードされたファイルに管轄外データがあります！！");
                            return;
                        }
                    }

                    UpDownPanel.this.info("saved file: " + upload.getClientFileName());
                   
                   
                    
                    SearchSession.get().setTempList(meibos);
                    PageParameters param = new PageParameters();
                    param.put("data", "set");
                    this.setResponsePage(AdminPage.class, param);
                   
                } catch (Exception e) {
                    this.error("読み込みに失敗しました！！"+e);
                    return;
                }

            }
        }
    }


    
    
}
