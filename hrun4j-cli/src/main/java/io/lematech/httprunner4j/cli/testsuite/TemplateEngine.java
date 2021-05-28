package io.lematech.httprunner4j.cli.testsuite;

import io.lematech.httprunner4j.common.DefinedException;
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
     *
     * @param templateName template of name
     * @param context      context
     * @param file         write to file
     */
    public static void writeToFileByTemplate(String templateName, File file, VelocityContext context) {

        Template template;
        try {
            template = getInstance().getTemplate(templateName);
        } catch (Exception e) {
            String exceptionMsg = String.format("There was an exception getting the template %s,Exception Information: %s", templateName, e.getMessage());
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
            String exceptionMsg = String.format("An exception occurred writing to template %s the file: %s,exception information: %s", templateName, file.getAbsolutePath(), e.getMessage());
            throw new DefinedException(exceptionMsg);
        }
    }

}

