# 关于hrun4j



<p align="center">
  <a href="https://www.lematech.vip/">
    <img width="200" src="https://cdn.lematech.vip/lematech_logo.png"></a>
</p>

<h1 align="center">
  <a href="https://www.lematech.vip/" target="_blank">hrun4j</a>
</h1>

[![TesterHome](https://img.shields.io/badge/TTF-TesterHome-2955C5.svg)](https://testerhome.com/github_statistics)

>`hrun4j`是由`乐马技术`推出的开源一站式接口测试解决方案，它不仅仅只是一个Java版的httprunner，现阶段规划解决方案包括四部分：


1. `hrun4j-core`：框架核心，提供完整的运行机制、数据驱动、多种表达式引擎、多种数据检查机制及测试报告生成（√）
2. `hrun4j-plugin`：Intellij Idea插件，赋能研发，插件式集成至IDEA，提供智能补全、快速填充及在线调试运行功能（实现中）
3. `hrun4j-platform`: 官方Web平台，赋能测试，提供完整的API测试生命周期管理（规划中）
4. `hrun4j-sync`: 同步中心，增强研发测试协作，采用双向同步机制，支持研发本地接口文档、用例上传或远程用例下载自测（规划中）


## ✨ 核心特性

1. 支持以`CLI`和`POM`模式集成`hrun4j`能力，如以`POM`模式集成，可以无缝融入Spring 生态链
2. 集成纯粹且优雅的`Okhttps`，即使是复杂场景（比如上传/下载进度控制），它都能轻松搞定
3. 借助`TestNG`实现YML或JSON格式数据驱动、测试用例组织与执行
4. 借助`ReportNG`，生成优雅详细的测试报告
5. 支持多种数据提取方式，比如：`正则表达式/Jsonpath/Jmespath/对象提取`，支持丰富的校验方式，比如：`startsWith/endsWith/equalTo/not/containsString`等
6. 内置强大的表达式引擎，支持`Aviator`和`BeanShell`脚本，借助他们即可轻松实现复杂的动态业务逻辑
7. 测试前后支持完善的`hook`机制
8. 提供强大且贴心CLI工具集，即`瑞士小军刀`，目前支持`har2yml|json`、`viewhar`、`run`、`startproject CLI/POM`、`swagger2api`、`postman2case`
9. 插件式集成至IDEA，提供智能补全、快速填充及在线调试运行功能（实现中）
10. 内置国际化支持，配置`I18N`参数即可轻松切换中英文输出
11. 框架核心实现充分利用JAVA语言特性，把面向对象、继承、设计模式及反射机制发挥淋漓尽致


## 支持环境

支持`Mac/Windows/Linux`操作系统，只需要用户配置`JDK`或`JRE`环境变量即可。

## 安装

参考`快速上手`篇

## 链接


### 乐马技术

- [乐马技术](https://www.lematech.vip/)
- [hrun4j官方文档](https://www.lematech.vip/docs/react/introduce-cn)

### 联系方式

微信：![-w376](http://cdn.lematech.vip/mweb/16257987099898.jpg)
如果入群方式已关闭，框架使用上若有疑问，可先加微信【wytest】（请备注 公司+地区+昵称）再入群交流
邮箱：lematech@foxmail.


### 友情链接

- [okhttps官方文档](https://github.com/ejlchina/okhttps)
- [bean-searcher官方文档](https://github.com/ejlchina/bean-searcher)
- [DebugTalk](https://debugtalk.com/)
- [TesterHome](https://testerhome.com/)
- **[ OkHttps ]**：[ 一个轻量级http通信框架，API设计无比优雅，支持 WebSocket 以及 Stomp 协议](https://gitee.com/ejlchina-zhxu/okhttps)
- **[ Bean Searcher ]**：[ 比 MyBatis 效率快 100 倍的条件检索引擎，天生支持联表，使一行代码实现复杂列表检索成为可能！](https://github.com/ejlchina/bean-searcher)

## 生态

1. `hrun4j-core`：框架核心，提供完整的运行机制、数据驱动、多种表达式引擎、多种数据检查机制及测试报告生成（√）--框架核心
2. `hrun4j-plugin`：Intellij Idea插件，赋能研发，插件式集成至IDEA，提供智能补全、快速填充及在线调试运行功能（实现中）---插件化
3. `hrun4j-platform`: 官方Web平台，赋能测试，提供完整的API测试生命周期管理（规划中）---平台化
4. `hrun4j-sync`: 同步中心，增强研发测试协作，采用双向同步机制，支持研发本地接口文档、用例上传或远程用例下载自测（规划中）---协助及赋能

## 鸣谢

1. 感谢DebugTalk（李隆），为行业带来这么优秀的测试框架HttpRunner，官方说明文档多处引用其官网

## 问答

> 强烈推荐阅读 [《提问的智慧》](https://github.com/ryanhanwu/How-To-Ask-Questions-The-Smart-Way)、[《如何向开源社区提问题》](https://github.com/seajs/seajs/issues/545) 和 [《如何有效地报告 Bug》](http://www.chiark.greenend.org.uk/%7Esgtatham/bugs-cn.html)、[《如何向开源项目提交无法解答的问题》](https://zhuanlan.zhihu.com/p/25795393)，更好的问题更容易获得帮助。

[![Let's fund issues in this repository](https://issuehunt.io/static/embed/issuehunt-button-v1.svg)](https://issuehunt.io/repos/104172832)

## 赞助

`hrun4j` 是遵循 Apache 协议的开源项目。为了项目能够更好的持续的发展，我们期望获得更多的支持者，你可以通过如下任何一种方式支持我们：

1. 支付宝或微信


## 项目划分

框架地址：https://github.com/lematechvip/hrun4j

工程目录如下：
```
├── hrun4j-api: 提供api接口方便扩展成平台
├── hrun4j-cli: 提供命令行支持，支持用例录制，可快速创建脚手架、测试用例集运行及调试，
├── hrun4j-core: 工程核心模块，提供完整的运行机制、数据驱动、表达式引擎及测试报告生成
├── hrun4j-plugins: 插件式集成至IDEA，提供智能补全、快速填充及在线调试运行功能
├── hrun4j-test-demo: 常用案例使用说明
├── hrun4j-test-server：内置测试服务，基于springboot开发，工程规范标准
```

## 更多信息

欢迎关注`乐马技术`微信公众号，第一时间获得最新资讯

![-w195](http://cdn.lematech.vip/mweb/16255583450362.jpg)


