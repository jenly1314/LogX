# LogX

[![Download](https://img.shields.io/badge/download-App-blue.svg)](https://raw.githubusercontent.com/jenly1314/LogX/master/app/release/app-release.apk)
[![MavenCentral](https://img.shields.io/maven-central/v/com.github.jenly1314/logx)](https://repo1.maven.org/maven2/com/github/jenly1314/logx)
[![JitPack](https://jitpack.io/v/jenly1314/LogX.svg)](https://jitpack.io/#jenly1314/LogX)
[![CircleCI](https://circleci.com/gh/jenly1314/LogX.svg?style=svg)](https://circleci.com/gh/jenly1314/LogX)
[![API](https://img.shields.io/badge/API-21%2B-blue.svg?style=flat)](https://android-arsenal.com/api?level=21)
[![License](https://img.shields.io/badge/license-Apche%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

**LogX** 一个小而美的日志记录框架；既有 **Timber** 的易用性与可扩展性，又具备 **Logger** 的日志格式美观性。

>写这个日志框架的主要原因是为了简化维护流程。在我个人的GitHub开源项目中，有一些需要使用日志功能的库。
>最初，我使用的是一个自维护日志工具类：LogUtils，当开源项目数量较少时，这种方法还比较有效。
>然而，随着开源项目数量的增加，我不得不频繁地复制和维护LogUtils，随着时间的推移，不同开源项目中的LogUtils可能会
>出现一些差异，这大大增加了维护的难度。因此，我开始考虑更加优雅的解决方案。在经过一段时间的思考和研究后，我决定
>结合平时使用的 [Timber](https://github.com/JakeWharton/timber) 和 [Logger](https://github.com/orhanobut/logger) 这
>两个成熟的开源库，取其精华，编写一个新的日志框架，即：**LogX** 。

## 类图

![Image](art/logx_uml.png)

> 从上面的类图可以明确的看出 **LogX** 内部之间的关系与结构。

## 引入

### Gradle:

1. 在Project的 **build.gradle** 或 **setting.gradle** 中添加远程仓库

    ```gradle
    repositories {
        //...
        mavenCentral()
    }
    ```

2. 在Module的 **build.gradle** 里面添加引入依赖项

    ```gradle
    implementation 'com.github.jenly1314:logx:1.1.0'
    ```

## 使用

### 基本用法

如果没有特殊的要求，直接使用即可。

> **LogX** 无需初始化，因为 **LogX** 的默认配置就是个人最推荐的配置。（这也是我写 **LogX** 的原因之一）

主要的一些方法调用示例如下：

```java
LogX.v("verbose");
LogX.d("debug");
LogX.i("info");
LogX.w("warn");
LogX.e("error");
LogX.wtf("assert");

```

占位符格式化示例如下:

```java
LogX.d("hello %s", "world");
```

> 当未指定tag时，内部会自动通过线程栈获取对应的简单类名来作为tag，上面的示例皆是如此；不过你也可以自己指定tag；

指定tag示例如下：

```java
// 指定tag（指定的tag是一次性的）
LogX.tag("MyTag").d("debug");
```

> 看了上面的基本用法，相信你已经会使用 **LogX** 了；如果还想了解更多，你可以继续往下看高级用法。

### 高级用法

如果你有特别的需求，你可以自己按照喜好进行配置`DefaultLogger`或自定义实现`Logger`。

```java

// 参数1：是否显示线程信息； 参数1：显示多少方法行，；参数3：方法栈偏移量
Logger logger = new DefaultLogger(true, 2, 0);
LogX.setLogger(logger);

```

全局配置是否记录日志示例如下：

```java
LogX.setLogger(new DefaultLogger() {
   @Override
   protected boolean isLoggable(int priority, @Nullable String tag) {
//       return super.isLoggable(priority, tag);
        // 例如：只在开发模式才记录日志
       return BuildConfig.DEBUG;
   }
});
```
> **LogX** 默认配置就是在debug包下才开启日志记录的，release包默认是关闭日志记录的。

使用`CompositeLogger`管理`Logger`示例如下：

```java
CompositeLogger compositeLogger = new CompositeLogger();
// 添加任意的Logger实现即可；例如：DefaultLogger
compositeLogger.addLogger(new DefaultLogger());
LogX.setLogger(compositeLogger);
```

> 如以上内置的功能都不满足你的需求，你还可以自定义实现一个`Logger`。

### 日志效果

日志的默认输出格式如下：

```
 ┌──────────────────────────────
 │ Thread information
 ├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄
 │ Method stack history
 ├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄
 │ Log message
 └──────────────────────────────
```
在Logcat中的实际效果如下：

![Image](art/logx_output.png)

### 特别说明

既然 **LogX** 的实现主要是参考了：**Timber** 和 **Logger**，那么二者的诸多优点，**LogX** 自然也是支持的。

比如：如果你之前是集成使用的Timber，那么现在你也可以通过如下方式，将最终的日志输出转到 **LogX**，让日志输出格式更具美观性。
```java
Timber.plant(new Timber.Tree() {
   @Override
   protected void log(int priority, @Nullable String tag, @NonNull String message, @Nullable Throwable throwable) {
//       if(tag != null) { // 这里的tag可要可不要
//           LogX.tag(tag);
//       }
       // 在Timber目前层级没发生变化的情况下，方法栈偏移4 即可准确定位到原始Timber调用的代码所在行。（这里不需要throwable，因为message里面已经包含了）
       LogX.offset(4).log(priority, message);
   }
});

```

> 通过使用`LogX.offset(offset)`进行方法栈的偏移，就算多库混用也互不影响，都可以轻松的定位到日志具体的代码行。

### 关于`FormatUtils`（v1.1.0新增）

格式化`json`和`xml`本不属于日志打印的范畴，但考虑到格式化后的`json`和`xml`能提升一定程度上的可读性与美观性；最终决定提供一个工具类`FormatUtils`来支持格式化功能；至于是否需要格式化，把决定权交给使用的开发者。

#### 格式化json示例：
```java
String json = "{\"key\": \"value\", \"array\":[\"item1\",\"item2\"]}";
// 打印格式化后的json
LogX.d(FormatUtils.formatJson(json));

```

#### 格式化xml示例：
```java
String xml = "<root><key>value</key><array><item>item1</item><item>item2</item></array></root>";
// 打印格式化后的xml
LogX.d(FormatUtils.formatXml(xml));
```

更多使用详情，请查看[app](app)中的源码使用示例或直接查看[API帮助文档](https://jenly1314.github.io/LogX/api/)

<!-- end -->

## 版本日志

#### v1.1.0：2024-12-10
* 新增格式化工具类：`FormatUtils`

#### [查看更多版本日志](CHANGELOG.md)

## 赞赏

如果你喜欢LogX，或感觉LogX帮助到了你，可以点右上角“Star”支持一下，你的支持就是我的动力，谢谢 :smiley:
<p>您也可以扫描下面的二维码，请作者喝杯咖啡 :coffee:

<div>
   <img src="https://jenly1314.github.io/image/page/rewardcode.png">
</div>

## 关于我

| 我的博客                                                                                | GitHub                                                                                  | Gitee                                                                                 | CSDN                                                                                | 博客园                                                                           |
|:------------------------------------------------------------------------------------|:----------------------------------------------------------------------------------------|:--------------------------------------------------------------------------------------|:------------------------------------------------------------------------------------|:------------------------------------------------------------------------------|
| <a title="我的博客" href="https://jenly1314.github.io" target="_blank">Jenly's Blog</a> | <a title="GitHub开源项目" href="https://github.com/jenly1314" target="_blank">jenly1314</a> | <a title="Gitee开源项目" href="https://gitee.com/jenly1314" target="_blank">jenly1314</a> | <a title="CSDN博客" href="http://blog.csdn.net/jenly121" target="_blank">jenly121</a> | <a title="博客园" href="https://www.cnblogs.com/jenly" target="_blank">jenly</a> |

## 联系我

| 微信公众号                                                   | Gmail邮箱                                                                          | QQ邮箱                                                                              | QQ群                                                                                                                       | QQ群                                                                                                                       |
|:--------------------------------------------------------|:---------------------------------------------------------------------------------|:----------------------------------------------------------------------------------|:--------------------------------------------------------------------------------------------------------------------------|:--------------------------------------------------------------------------------------------------------------------------|
| [Jenly666](http://weixin.qq.com/r/wzpWTuPEQL4-ract92-R) | <a title="给我发邮件" href="mailto:jenly1314@gmail.com" target="_blank">jenly1314</a> | <a title="给我发邮件" href="mailto:jenly1314@vip.qq.com" target="_blank">jenly1314</a> | <a title="点击加入QQ群" href="https://qm.qq.com/cgi-bin/qm/qr?k=6_RukjAhwjAdDHEk2G7nph-o8fBFFzZz" target="_blank">20867961</a> | <a title="点击加入QQ群" href="https://qm.qq.com/cgi-bin/qm/qr?k=Z9pobM8bzAW7tM_8xC31W8IcbIl0A-zT" target="_blank">64020761</a> |

<div>
   <img src="https://jenly1314.github.io/image/page/footer.png">
</div>

