package io.lematech.httprunner4j.core.engine;

import cn.hutool.core.io.FileUtil;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.widget.utils.FilesUtil;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;


/**
 * @author lematech@foxmail.com
 * @version 1.0.0
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

    /**
     * render template by context self-defined variables
     *
     * @param templateName template of name
     * @param context      context
     * @param file         write to file
     * @return The contents of the template after rendering
     */
    public static void writeToFileByTemplate(String templateName, File file, VelocityContext context) {

        Template template;
        try {
            template = getInstance().getTemplate(templateName);
        } catch (Exception e) {
            String exceptionMsg = String.format("There was an exception getting the template %s,Exception Information: ", templateName, e.getMessage());
            throw new DefinedException(exceptionMsg);
        }

        try {
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));
            template.merge(context, outputStreamWriter);
            outputStreamWriter.flush();
            outputStreamWriter.close();
        } catch (Exception e) {
            String exceptionMsg = String.format("An exception occurred writing to template %s the file: %s", templateName, file.getAbsolutePath());
            throw new DefinedException(exceptionMsg);
        }
    }

}

