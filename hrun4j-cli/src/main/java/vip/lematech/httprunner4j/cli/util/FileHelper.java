package vip.lematech.httprunner4j.cli.util;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.io.resource.ResourceUtil;
import sun.net.www.protocol.file.FileURLConnection;
import vip.lematech.httprunner4j.helper.LogHelper;

import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className FileHelper
 * @description TODO
 * @created 2021/6/15 3:13 下午
 * @publicWechat lematech
 */
public class FileHelper {
    public void loadRecourseFromJarByFolder(String folderPath,String targetFolderPath) throws IOException {
       // URL url = this.getClass().getClassLoader().getResource(folderPath);
        ClassPathResource resource = new ClassPathResource(folderPath);
        URL url = resource.getUrl();
        URLConnection urlConnection = url.openConnection();
        if (urlConnection instanceof FileURLConnection) {
            copyFileResources(url, folderPath,targetFolderPath);
        } else if (urlConnection instanceof JarURLConnection) {
            copyJarResources((JarURLConnection) urlConnection,folderPath,targetFolderPath);
        }
    }

    /**
     * 当前运行环境资源文件是在文件里面的
     *
     * @param url
     * @param folderPath
     * @throws IOException
     */
    private  void copyFileResources(URL url, String folderPath,String targetFolderPath) throws IOException {
        File root = new File(url.getPath());
        if (root.isDirectory()) {
            File[] files = root.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    loadRecourseFromJarByFolder(folderPath + "/" + file.getName(),targetFolderPath);
                } else {
                    loadRecourseFromJar(folderPath + "/" + file.getName(),folderPath);
                }
            }
        }
    }

    /**
     * 当前运行环境资源文件是在jar里面的
     *
     * @param jarURLConnection
     * @throws IOException
     */
    private void copyJarResources(JarURLConnection jarURLConnection,String folderPath,String targetFolderPath) throws IOException {
        JarFile jarFile = jarURLConnection.getJarFile();
        Enumeration<JarEntry> entrys = jarFile.entries();
        while (entrys.hasMoreElements()) {
            JarEntry entry = entrys.nextElement();
            if (entry.getName().startsWith(jarURLConnection.getEntryName()) && !entry.getName().endsWith("/")) {
                loadRecourseFromJar("/" + entry.getName(),targetFolderPath);
            }
        }
        jarFile.close();
    }

    public void loadRecourseFromJar(String path,String recourseFolder) throws IOException {
       /* if (!path.startsWith("/") || !path.startsWith("file:")) {
            throw new IllegalArgumentException("The path has to be absolute (start with '/').");
        }

        if (path.endsWith("/")) {
            throw new IllegalArgumentException("The path has to be absolute (cat not end with '/').");
        }*/

        int index = path.lastIndexOf('/');

        String filename = path.substring(index + 1);
        String folderPath = recourseFolder + path.substring(0, index + 1);

        // If the folder does not exist yet, it will be created. If the folder
        // exists already, it will be ignored
        File dir = new File(folderPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // If the file does not exist yet, it will be created. If the file
        // exists already, it will be ignored
        filename = folderPath + filename;
        File file = new File(filename);

        if (!file.exists() && !file.createNewFile()) {
            LogHelper.error("create file :{} failed .fileName:"+ filename);
            return;
        }

        // Prepare buffer for data copying
        byte[] buffer = new byte[1024];
        int readBytes;

        // Open and check input stream
        URL url = this.getClass().getResource(path);
        URLConnection urlConnection = url.openConnection();
        InputStream is = urlConnection.getInputStream();

        if (is == null) {
            throw new FileNotFoundException("File " + path + " was not found inside JAR.");
        }
        OutputStream os = new FileOutputStream(file);
        try {
            while ((readBytes = is.read(buffer)) != -1) {
                os.write(buffer, 0, readBytes);
            }
        } finally {
            os.close();
            is.close();
        }

    }
    public static void main(String[] args) throws IOException {
        FileHelper fileHelper = new FileHelper();
        fileHelper.loadRecourseFromJarByFolder("/hunspell-win-x86-64.dll", "D:\\test");
        fileHelper.loadRecourseFromJarByFolder("/META-INF/maven", "D:\\test");
    }
}
