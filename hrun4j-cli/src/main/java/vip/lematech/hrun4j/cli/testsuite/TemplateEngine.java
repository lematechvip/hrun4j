package vip.lematech.hrun4j.cli.testsuite;

import cn.hutool.core.io.FileUtil;
import vip.lematech.hrun4j.common.Constant;
import vip.lematech.hrun4j.common.DefinedException;
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
     *
     * @param templateName template of name
     * @param context      context
     * @param file         write to file
     */
    public static void writeToFileByTemplate(String templateName, File file, VelocityContext context) {

        Template template;
        try {
            template = getInstance().getTemplate(templateName, Constant.CHARSET_UTF_8);
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

            StringWriter sw = new StringWriter();
            try {
                template.merge(context, sw);
            } catch (Exception e) {
                String exceptionMsg = String.format("An exception occurred in the rendering engine template %s based on the constructed data,,Exception Informations: %s", templateName, e.getMessage());
                throw new DefinedException(exceptionMsg);
            }

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));
            String content = sw.toString();
            String extName = FileUtil.extName(file);
            if (Constant.SUPPORT_TEST_CASE_FILE_EXT_YML_NAME.equals(extName)) {
                String contentValue = content.replaceAll("\\\\\\{", "{")
                        .replaceAll("\\\\\\}", "}")
                        .replaceAll("\\\\\\(", "(")
                        .replaceAll("\\\\\\)", ")");
                content = contentValue;
            }
            outputStreamWriter.write(content);
            outputStreamWriter.flush();
            outputStreamWriter.close();
        } catch (Exception e) {
            String exceptionMsg = String.format("An exception occurred writing to template %s the file: %s,exception information: %s", templateName, file.getAbsolutePath(), e.getMessage());
            throw new DefinedException(exceptionMsg);
        }
    }

}

