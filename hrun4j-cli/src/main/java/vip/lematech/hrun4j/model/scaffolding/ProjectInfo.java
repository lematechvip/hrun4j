package vip.lematech.hrun4j.model.scaffolding;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
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
