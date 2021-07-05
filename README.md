
# 关于hrun4j

<p align="center">
  <a href="https://www.lematech.vip/">
    <img width="200" src="https://cdn.lematech.vip/lematech_logo.png"></a>
</p>

<h1 align="center">
  <a href="https://www.lematech.vip/" target="_blank">hrun4j</a>
</h1>

>Hrun4j是由`乐马技术`推出的开源一站式接口测试解决方案，它不仅仅只是一个Java版的httprunner，现阶段规划解决方案包括四部分：

1. `hrun4j-core`：框架核心，提供完整的运行机制、数据驱动、多种表达式引擎、多种数据检查机制及测试报告生成（√）
2. `hrun4j-plugin`：Intellij Idea插件，赋能研发，插件式集成至IDEA，提供智能补全、快速填充及在线调试运行功能（实现中）
3. `hrun4j-platform`: 官方Web平台，赋能测试，提供完整的API测试生命周期管理（规划中）
4. `hrun4j-sync`: 同步中心，增强研发测试协作，采用双向同步机制，支持研发本地接口文档、用例上传或远程用例下载自测（规划中）

[简体中文](./README.md) | English

##✨ 核心特性

1. 支持以`CLI`和`POM`模式集成`hrun4j`能力，如以`POM`模式集成，可以无缝融入Spring生态链
2. 集成纯粹且优雅的`Okhttps`，即使是复杂场景（比如上传/下载进度控制），它都能轻松搞定
3. 借助`TestNG`实现YML或JSON格式数据驱动、测试用例组织与执行
4. 借助`ReportNG`，生成优雅详细的测试报告
5. 支持多种数据提取方式，比如：`正则表达式/Jsonpath/Jmespath`，支持丰富的校验方式，比如：`startsWith/endsWith/equalTo/not/containsString`等
6. 内置强大的表达式引擎，支持`Aviator`和`BeanShell`脚本，借助他们即可轻松实现复杂的动态业务逻辑
7. 测试前后支持完善的`hook`机制
8. 提供强大且贴心CLI工具集，即`瑞士小军刀`，目前支持`har2yml|json`、`viewhar`、`run`、`startproject CLI/POM`、`swagger2api`
9. 插件式集成至IDEA，提供智能补全、快速填充及在线调试运行功能（实现中）
10. 内置国际化支持，配置`I18N`参数即可轻松切换中英文输出
11. 框架核心实现充分利用JAVA语言特性，把面向对象、继承、设计模式及反射机制发挥淋漓尽致
