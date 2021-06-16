package vip.lematech.httprunner4j.model.scaffolding;

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
public class ProjectInfo {
    private String groupId;
    private String artifactId;
    private String version;
    private String name;
    private String description;
}
