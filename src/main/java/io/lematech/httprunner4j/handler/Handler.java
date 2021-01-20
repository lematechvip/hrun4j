package io.lematech.httprunner4j.handler;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className Handler
 * @description TODO
 * @created 2021/1/20 6:27 下午
 * @publicWechat lematech
 */
public class Handler {

    public void loadFile(String name){
        Yaml yaml = new Yaml();
        File f=new File("src/test/resources/testcases/"+name+".yml");
        //读入文件
        try {
            Object result= yaml.load(new FileInputStream(f));
            System.out.println(result.getClass());
            System.out.println( result);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
