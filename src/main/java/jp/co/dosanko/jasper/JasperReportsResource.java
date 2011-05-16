package jp.co.dosanko.jasper;

/**
 *
 * @author igahito
 */
import java.io.ByteArrayOutputStream;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.apache.wicket.markup.html.DynamicWebResource;
import org.apache.wicket.protocol.http.WebResponse;

public abstract class JasperReportsResource extends DynamicWebResource {

    private static final long serialVersionUID = 1L;
    
    private String name;

    public JasperReportsResource() {

    }
    
    public JasperReportsResource setFileName(String name){
        this.name=name;
        return this;
    }

    @Override
    protected ResourceState getResourceState() {

        return new ResourceState() {

            @Override
            public String getContentType() {

                return "application/pdf";

            }

            @Override
            public byte[] getData() {
                JRPdfExporter exporter = new JRPdfExporter();

                ByteArrayOutputStream os = new ByteArrayOutputStream();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, JasperReportsResource.this.getJasperPrint());
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
                try {
                    exporter.exportReport();

                } catch (JRException e) {
                }

                return os.toByteArray();

            }
        };

    }

    @Override
    protected void setHeaders(WebResponse response) {
        super.setHeaders(response);
        if(this.name!=null)
            response.setAttachmentHeader(this.name+".pdf");
    }
    
    public abstract JasperPrint getJasperPrint();
    
    
}
