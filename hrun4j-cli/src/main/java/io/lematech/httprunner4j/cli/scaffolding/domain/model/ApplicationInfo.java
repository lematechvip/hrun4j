package io.lematech.httprunner4j.cli.scaffolding.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationInfo {
    private String packageName;
    private String className;
    public ApplicationInfo(String packageName) {
        this.packageName = packageName;
    }
}
