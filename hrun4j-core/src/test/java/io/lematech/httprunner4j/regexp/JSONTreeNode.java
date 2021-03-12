package io.lematech.httprunner4j.regexp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className TreeNode
 * @description TODO
 * @created 2021/2/2 9:47 上午
 * @publicWechat lematech
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class JSONTreeNode {
    private Object data;
    private HashMap<String,Object> children;
}