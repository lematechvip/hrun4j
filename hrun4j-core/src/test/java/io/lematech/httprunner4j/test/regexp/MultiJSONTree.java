package io.lematech.httprunner4j.test.regexp;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className MultiTree
 * @description TODO
 * @created 2021/2/2 9:47 上午
 * @publicWechat lematech
 */
@Slf4j
public class MultiJSONTree {
    private JSONTreeNode root = new JSONTreeNode();
    private List<String> pathList = new ArrayList<>();

    public MultiJSONTree(HashMap<String, Object> res) {

    }



    public List<String> listAllPathByRecursion() {
        //清空路径容器
        this.pathList.clear();
        listPath(this.root, "");
        return this.pathList;
    }

    public void listPath(JSONTreeNode root, String path) {
        if (root.getChildren().isEmpty()) {//叶子节点
            path = path + root.getData().toString();
            pathList.add(path); //将结果保存在list中
            return;
        } else { //非叶子节点

            //进行子节点的递归
            MyData data = JSON.parseObject(JSON.toJSONString(root.getData()), MyData.class);
            path = path + data.getText() + "->";
            HashMap<String, Object> childs = root.getChildren();
            Iterator iterator = childs.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry mydata = (Map.Entry) iterator.next();
                Map.Entry myChild = (Map.Entry) iterator.next();
                //   JSONTreeNode childNode  = (JSONTreeNode) entry.getValue();

                // childNode.getChildren().get("")

                //root.setChildren(children);
                // root.setData(children.get("data"));
                Object key = myChild.getKey();
                Object val = myChild.getValue();
                log.info("key：{},value：{}", key, val);
                //JSONObject.parseObject()
                // HashMap<String, Object> children = (HashMap)entry.getValue().get("root");
                // root.setChildren(children);
                //  root.setData(children.get("data"));
                JSONTreeNode childNode = new JSONTreeNode();
                childNode.setData(mydata);
                childNode.setChildren((HashMap) myChild.getValue());
                listPath(childNode, path);
            }
        }
    }
}
