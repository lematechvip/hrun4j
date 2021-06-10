# 工程案例

>本工程由Httprunner4j脚手架工具生成，该工程以POM方式集成httprunner4j能力


```
├── README.md  案例说明
├── firstProject.iml
├── pom.xml POM案例
└── src
├── main
│   └── java
│       └── io
│           └── lematech
│               └── firstproject
│                   └── testcases
│                       ├── HttpRunner4j.java --工程关键，继承Httprunner4j类即可引入框架，另外配置、动态表达式引入可在此处完成
│                       └── functions  -- 自定义方法集合
│                           └── JokeFunction.java  -- 方法实现
└── test
├── java
│   └── io
│       └── lematech
│           └── firstproject
│               └── testcases
│                   ├── joke
│                   │   └── JokeTest.java  -- 单测用例，一个目录下只需一个，目录名称要喝resources/testcases/joke对应
│                   └── rap2
│                       └── MockTest.java -- 单测用例，一个目录下只需一个，目录名称要喝resources/testcases/rap2对应
└── resources
├── apis  -- 接口定义
│   ├── getJoke.yml
│   └── getSingleJoke.yml
├── data -- 数据文件
├── testcases --用例文件
│   ├── joke
│   │   └── lookTheJokeFromJokeList.yml
│   └── rap2
│       └── rap2Mock.yml
└── testsuite  --用例集文件夹
├── testsuite.xml   -- 用例集
└── testsuite_joke.xml -- 用例集

```


构建命令：`mvn clean test -DxmlFileName=src/test/resources/testsuite/testsuite.xml`

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>2.17</version>
    <configuration>
        <suiteXmlFiles>
            <suiteXmlFile>${xmlFileName}</suiteXmlFile>
        </suiteXmlFiles>
    </configuration>
</plugin>
```
其中：
1. xmlFileName代表pom.xml配置的suiteXmlFile值
2. `src/test/resources/testsuite/testsuite.xml`当前工程下测试用例集相对或绝对路径

