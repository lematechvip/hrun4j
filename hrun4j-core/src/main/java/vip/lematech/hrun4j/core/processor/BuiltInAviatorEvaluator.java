package vip.lematech.hrun4j.core.processor;


import bsh.EvalError;
import bsh.Interpreter;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.util.StrUtil;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;
import com.googlecode.aviator.runtime.type.AviatorString;
import vip.lematech.hrun4j.base.TestBase;
import vip.lematech.hrun4j.entity.http.RequestEntity;
import vip.lematech.hrun4j.helper.LogHelper;
import vip.lematech.hrun4j.common.Constant;
import vip.lematech.hrun4j.common.DefinedException;
import vip.lematech.hrun4j.config.Env;
import vip.lematech.hrun4j.config.RunnerConfig;
import vip.lematech.hrun4j.entity.http.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.testng.collections.Maps;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * build in aviator evaluator
 *
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
 */
@Slf4j
public class BuiltInAviatorEvaluator {
    static {
        AviatorEvaluator.addFunction(new BuiltInFunctionEnv());
        AviatorEvaluator.addFunction(new BuiltInFunctionParameterize());
        AviatorEvaluator.addFunction(new BuiltInFunctionHelloWorld());
        AviatorEvaluator.addFunction(new BuiltInFunctionBeanShell());
    }

    public static Object execute(String expression, Map<String, Object> env) {
        try {
            return AviatorEvaluator.execute(expression, env, false);
        } catch (Exception e) {
            String exceptionMsg = String.format("Execute exp %s occur error: %s", expression, e.getLocalizedMessage());
            throw new DefinedException(exceptionMsg);
        }
    }

    /**
     * built-in helloWorld
     */
    public static class BuiltInFunctionHelloWorld extends AbstractFunction {
        @Override
        public AviatorObject call(Map<String, Object> env) {
            String output = "Hello,hrun4j";
            LogHelper.info(output);
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
     * built-in $BSH
     */
    public static class BuiltInFunctionBeanShell extends AbstractFunction {
        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject bshFilePathObj) {
            Object bshFile = bshFilePathObj.getValue(env);
            if (Objects.isNull(bshFile) || StrUtil.isEmpty(bshFile.toString())) {
                String exceptionMsg = String.format("The bsh file path cannot be empty");
                throw new DefinedException(exceptionMsg);
            }
            String bshFilePathValue = bshFile.toString();
            if (!Constant.BEANSHELL_EXTNAME.equalsIgnoreCase(FileUtil.extName(bshFilePathValue))) {
                String exceptionMsg = String.format("BeanShell scripts must have a.bsh suffix");
                throw new DefinedException(exceptionMsg);
            }
            String workDirPath;
            RunnerConfig.RunMode runMode = RunnerConfig.getInstance().getRunMode();
            File bshFilePath = null;
            if (runMode == RunnerConfig.RunMode.POM) {
                workDirPath = TestBase.class.getClassLoader().getResource("").getPath();
                bshFilePath = new File(workDirPath, bshFilePathValue);
                if (!bshFilePath.exists() || !bshFilePath.isFile()) {
                    String exceptionMsg = String.format("The bsh file %s does not exist,Note that in API mode, absolute paths are not allowed, only class paths can be used"
                            , FileUtil.getAbsolutePath(bshFilePath));
                    throw new DefinedException(exceptionMsg);
                }
            } else if (runMode == RunnerConfig.RunMode.CLI) {
                workDirPath = FileUtil.getAbsolutePath(RunnerConfig.getInstance().getWorkDirectory());
                if (!FileUtil.isAbsolutePath(bshFilePathValue)) {
                    bshFilePath = new File(workDirPath, bshFilePathValue);
                    if (!bshFilePath.exists() || !bshFilePath.isFile()) {
                        String exceptionMsg = String.format("The bsh file %s does not exist"
                                , FileUtil.getAbsolutePath(bshFilePath));
                        throw new DefinedException(exceptionMsg);
                    }
                } else {
                    bshFilePath = new File(bshFilePathValue);
                    if (!bshFilePath.exists() || !bshFilePath.isFile()) {
                        String exceptionMsg = String.format("The bsh file %s does not exist"
                                , FileUtil.getAbsolutePath(bshFilePath));
                        throw new DefinedException(exceptionMsg);
                    }
                }
            }
            Interpreter interpreter = new Interpreter();
            Object result;
            try {
                RequestEntity requestEntity = (RequestEntity) env.get(Constant.REQUEST_VARIABLE_NAME);
                interpreter.set(Constant.REQUEST_VARIABLE_NAME, requestEntity);
                ResponseEntity responseEntity = (ResponseEntity) env.get(Constant.RESPONSE_VARIABLE_NAME);
                interpreter.set(Constant.RESPONSE_VARIABLE_NAME, responseEntity);
                interpreter.set("$ENV", env);
                env.remove(Constant.RESPONSE_VARIABLE_NAME);
                env.remove(Constant.REQUEST_VARIABLE_NAME);
                result = interpreter.source(FileUtil.getAbsolutePath(bshFilePath));
            } catch (IOException e) {
                String exceptionMsg = String.format("The bsh file %s does not exist"
                        , FileUtil.getAbsolutePath(bshFilePath));
                throw new DefinedException(exceptionMsg);
            } catch (EvalError evalError) {
                evalError.printStackTrace();
                String exceptionMsg = String.format("The BeanShell script executes an exception. Exception information: %s"
                        , evalError.getMessage());
                throw new DefinedException(exceptionMsg);
            }
            return AviatorRuntimeJavaType.valueOf(result);
        }
        @Override
        public String getName() {
            return "BSH";
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
            File csvFilePath = null;
            if (runMode == RunnerConfig.RunMode.POM) {
                workDirPath = TestBase.class.getClassLoader().getResource("").getPath();
                csvFilePath = new File(workDirPath, csvFilePathValue);
                if (!csvFilePath.exists() || !csvFilePath.isFile()) {
                    String exceptionMsg = String.format("The csv file %s does not exist,Note that in API mode, absolute paths are not allowed, only class paths can be used"
                            , FileUtil.getAbsolutePath(csvFilePath));
                    throw new DefinedException(exceptionMsg);
                }
            } else if (runMode == RunnerConfig.RunMode.CLI) {
                workDirPath = FileUtil.getAbsolutePath(RunnerConfig.getInstance().getWorkDirectory());
                if (!FileUtil.isAbsolutePath(csvFilePathValue)) {
                    csvFilePath = new File(workDirPath, csvFilePathValue);
                    if (!csvFilePath.exists() || !csvFilePath.isFile()) {
                        String exceptionMsg = String.format("The csv file %s does not exist"
                                , FileUtil.getAbsolutePath(csvFilePath));
                        throw new DefinedException(exceptionMsg);
                    }
                } else {
                    csvFilePath = new File(csvFilePathValue);
                    if (!csvFilePath.exists() || !csvFilePath.isFile()) {
                        String exceptionMsg = String.format("The csv file %s does not exist"
                                , FileUtil.getAbsolutePath(csvFilePath));
                        throw new DefinedException(exceptionMsg);
                    }
                }
            }
            CsvReader reader = CsvUtil.getReader();
            CsvData data = reader.read(csvFilePath, Charset.defaultCharset());
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
