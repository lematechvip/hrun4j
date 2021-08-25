package vip.lematech.hrun4j.core.engine;

import vip.lematech.hrun4j.common.DefinedException;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.StringWriter;


/**
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
 */

public class TemplateEngine {
    private static VelocityEngine velocityEngine;

    /**
     * get velocity engine instance
     *
     * @return velocity engine instance
     */
    private static synchronized VelocityEngine getInstance() {
        if (velocityEngine == null) {
            velocityEngine = new VelocityEngine();
            velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
            velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
            try {
                velocityEngine.init();
            } catch (Exception e) {
                String exceptionMsg = String.format("Velocity engine init exception :%s", e.getMessage());
                throw new DefinedException(exceptionMsg);
            }
        }
        return velocityEngine;
    }

    /**
     * render template by context self-defined variables
     * @param templateName template of name
     * @param context context
     * @return The contents of the template after rendering
     */
    public static String getTemplateRenderContent(String templateName, VelocityContext context){
        Template template;
        try {
            template = getInstance().getTemplate(templateName);
        } catch (Exception e) {
            String exceptionMsg = String.format("There was an exception getting the template %s,Exception Informations: ", templateName, e.getMessage());
            throw new DefinedException(exceptionMsg);
        }

        StringWriter sw = new StringWriter();
        try {
            template.merge(context, sw);
        } catch (Exception e) {
            String exceptionMsg = String.format("An exception occurred in the rendering engine template %s based on the constructed data,,Exception Informations: %s", templateName, e.getMessage());
            throw new DefinedException(exceptionMsg);
        }
        return sw.toString();
    }


}

