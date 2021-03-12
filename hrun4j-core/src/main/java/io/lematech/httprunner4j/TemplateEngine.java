package io.lematech.httprunner4j;

import io.lematech.httprunner4j.common.DefinedException;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.StringWriter;


public class TemplateEngine {
    private static VelocityEngine velocityEngine;
    public static synchronized VelocityEngine getInstance(){
        if(velocityEngine == null){
            velocityEngine = new VelocityEngine();
            velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
            velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
            try {
                velocityEngine.init();
            } catch (Exception e) {
                String exceptionMsg = String.format("velocity engine init exception :%s",e.getMessage());
                throw new DefinedException(exceptionMsg);
            }
        }
        return velocityEngine;
    }

    /**
     * render template by context self-defined variables
     * @param templateName
     * @param context
     * @return
     */
    public static String getTemplateRenderContent(String templateName, VelocityContext context){
        Template template;
        try {
            template = getInstance().getTemplate(templateName);
        } catch (Exception e) {
            String exceptionMsg = String.format("get template %s occur exception :",templateName,e.getMessage());
            throw new DefinedException(exceptionMsg);
        }
        StringWriter sw = new StringWriter();
        try {
            template.merge(context,sw);
        } catch (Exception e) {
            String exceptionMsg = String.format("paramete template %s occur exception :",templateName,e.getMessage());
            throw new DefinedException(exceptionMsg);
        }
        return sw.toString();
    }

}

