package vip.lematech.hrun4j.model.scaffolding;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * website http://lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationInfo {
    private String packageName;
    private String className;
}