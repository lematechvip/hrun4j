package io.lematech.httprunner4j.widget.exp;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.util.StrUtil;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorJavaType;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;
import com.googlecode.aviator.runtime.type.AviatorString;
import io.lematech.httprunner4j.base.TestBase;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.config.Env;
import io.lematech.httprunner4j.config.RunnerConfig;
import io.lematech.httprunner4j.widget.log.MyLog;
import io.lematech.httprunner4j.widget.utils.FilesUtil;
import lombok.extern.slf4j.Slf4j;
import org.testng.collections.Maps;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className BuiltInAviatorEvaluator
 * @description build in aviator evaluator
 * @created 2021/1/25 2:06 下午
 * @publicWechat lematech
 */
@Slf4j
public class BuiltInAviatorEvaluator {
    static {
        AviatorEvaluator.addFunction(new BuiltInFunctionEnv());
        AviatorEvaluator.addFunction(new BuiltInFunctionParameterize());
        AviatorEvaluator.addFunction(new BuiltInFunctionHelloWorld());
        AviatorEvaluator.addFunction(new HttpRunner4j.DefinedHookFunction());
        AviatorEvaluator.addFunction(new HttpRunner4j.DefinedFunctionAdd());
        AviatorEvaluator.addFunction(new HttpRunner4j.DefinedFunctionSubtract());
        AviatorEvaluator.addFunction(new HttpRunner4j.DefinedFunctionMultiply());
        AviatorEvaluator.addFunction(new HttpRunner4j.DefinedFunctionDivide());
        AviatorEvaluator.addFunction(new HttpRunner4j.SignGenerateFunction());
        AviatorEvaluator.addFunction(new HttpRunner4j.ReuqestAndResponseHook());
    }

    public static Object execute(String expression, Map<String, Object> env) {
        try {
            return AviatorEvaluator.execute(expression, env, false);
        } catch (Exception e) {
            String exceptionMsg = String.format("execute exp %s occur error: %s", expression, e.getMessage());
            throw new DefinedException(exceptionMsg);
        }
    }

    /**
     * built-in helloWorld
     */
    public static class BuiltInFunctionHelloWorld extends AbstractFunction {
        @Override
        public AviatorObject call() {
            String output = "Hello,HttpRunner!!!";
            MyLog.info(output);
            return new AviatorString(output);
        }

        @Override
        public String getName() {
            return "helloWorld";
        }
    }

    /**
     * built-in $ENV
     */
    public static class BuiltInFunctionEnv extends AbstractFunction {
        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
            Object envValue = arg1.getValue(Env.getEnvMap());
            if (Objects.isNull(envValue)) {
                String exceptionMsg = String.format("env not found key : %s", arg1);
                throw new DefinedException(exceptionMsg);
            }
            return new AviatorString(String.valueOf(envValue));
        }

        @Override
        public String getName() {
            return "ENV";
        }
    }

    /**
     * built-in $P
     */
    public static class BuiltInFunctionParameterize extends AbstractFunction {
        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject csvFilePathObj) {
            Object csvFile = csvFilePathObj.getValue(env);
            if (Objects.isNull(csvFile) || StrUtil.isEmpty(csvFile.toString())) {
                String exceptionMsg = String.format("The CVS file path cannot be empty");
                throw new DefinedException(exceptionMsg);
            }
            String csvFilePathValue = csvFile.toString();
            String workDirPath;
            RunnerConfig.RunMode runMode = RunnerConfig.getInstance().getRunMode();
            File cvsFilePath = null;
            if (runMode == RunnerConfig.RunMode.API) {
                workDirPath = TestBase.class.getClassLoader().getResource("").getPath();
                cvsFilePath = new File(workDirPath, csvFilePathValue);
                if (!cvsFilePath.exists() || !cvsFilePath.isFile()) {
                    String exceptionMsg = String.format("The CVS file %s does not exist,Note that in API mode, absolute paths are not allowed, only class paths can be used"
                            , FileUtil.getAbsolutePath(cvsFilePath));
                    throw new DefinedException(exceptionMsg);
                }
            } else if (runMode == RunnerConfig.RunMode.CLI) {
                workDirPath = FileUtil.getAbsolutePath(RunnerConfig.getInstance().getWorkDirectory());
                if (!FileUtil.isAbsolutePath(csvFilePathValue)) {
                    cvsFilePath = new File(workDirPath, csvFilePathValue);
                    if (!cvsFilePath.exists() || !cvsFilePath.isFile()) {
                        String exceptionMsg = String.format("The CVS file %s does not exist"
                                , FileUtil.getAbsolutePath(cvsFilePath));
                        throw new DefinedException(exceptionMsg);
                    }
                } else {
                    cvsFilePath = new File(csvFilePathValue);
                    if (!cvsFilePath.exists() || !cvsFilePath.isFile()) {
                        String exceptionMsg = String.format("The CVS file %s does not exist"
                                , FileUtil.getAbsolutePath(cvsFilePath));
                        throw new DefinedException(exceptionMsg);
                    }
                }
            }
            CsvReader reader = CsvUtil.getReader();
            CsvData data = reader.read(cvsFilePath, Charset.defaultCharset());
            List<CsvRow> rows = data.getRows();
            List<Map<String, Object>> csvParameters = new ArrayList<>();
            Map<Integer, String> parameterNameIndexMap = Maps.newHashMap();
            for (int index = 0; index < rows.size(); index++) {
                CsvRow csvRow = rows.get(index);
                if (index == 0) {
                    for (int columnIndex = 0; columnIndex < csvRow.size(); columnIndex++) {
                        parameterNameIndexMap.put(columnIndex, csvRow.get(columnIndex));
                    }
                } else {
                    Map<String, Object> parameter = Maps.newHashMap();
                    for (int columnIndex = 0; columnIndex < csvRow.size(); columnIndex++) {
                        String paramName = parameterNameIndexMap.get(columnIndex);
                        String paramValue = csvRow.get(columnIndex);
                        parameter.put(paramName, paramValue);
                    }
                    csvParameters.add(parameter);
                }
            }
            return AviatorRuntimeJavaType.valueOf(csvParameters);
        }

        @Override
        public String getName() {
            return "P";
        }
    }


}
