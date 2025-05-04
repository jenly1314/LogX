package com.king.logx.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.king.logx.LogX
import com.king.logx.app.databinding.ActivityMainBinding
import com.king.logx.logger.LogFormat
import com.king.logx.util.FormatUtils

/**
 * LogX 示例
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        LogX.v("verbose")
        LogX.d("debug")
        LogX.i("info")
        LogX.w("warn")
        LogX.e("error")

        // 单次设置日志tag并打印日志
        LogX.tag("LogX").d("hello %s", "world")

        // 单次指定日志显示格式并打印日志
        LogX.format(LogFormat.PLAIN).d("Original log message")

        val longText = "Long text: {\"description\":\"这是一个用于测试的超长JSON数据示例\",\"metadata\":{\"createdAt\":\"2023-05-15T14:30:00Z\",\"version\":\"1.0.0\",\"author\":\"测试工程师\",\"tags\":[\"测试\",\"JSON\",\"大数据\",\"性能\",\"验证\"]},\"users\":[{\"id\":1,\"name\":\"张三\",\"email\":\"zhangsan@example.com\",\"address\":{\"street\":\"人民路123号\",\"city\":\"北京\",\"zipcode\":\"100000\",\"coordinates\":{\"latitude\":39.9042,\"longitude\":116.4074}},\"orders\":[{\"orderId\":\"ORD-1001\",\"date\":\"2023-01-15\",\"items\":[{\"productId\":\"P001\",\"name\":\"智能手机\",\"quantity\":1,\"price\":5999},{\"productId\":\"P045\",\"name\":\"手机壳\",\"quantity\":2,\"price\":49}],\"total\":6097},{\"orderId\":\"ORD-1042\",\"date\":\"2023-03-22\",\"items\":[{\"productId\":\"P112\",\"name\":\"无线耳机\",\"quantity\":1,\"price\":399},{\"productId\":\"P087\",\"name\":\"充电宝\",\"quantity\":1,\"price\":199}],\"total\":598}]},{\"id\":2,\"name\":\"李四\",\"email\":\"lisi@example.com\",\"address\":{\"street\":\"南京路456号\",\"city\":\"上海\",\"zipcode\":\"200000\",\"coordinates\":{\"latitude\":31.2304,\"longitude\":121.4737}},\"orders\":[{\"orderId\":\"ORD-1087\",\"date\":\"2023-02-10\",\"items\":[{\"productId\":\"P201\",\"name\":\"笔记本电脑\",\"quantity\":1,\"price\":8999},{\"productId\":\"P045\",\"name\":\"鼠标\",\"quantity\":1,\"price\":129},{\"productId\":\"P046\",\"name\":\"键盘\",\"quantity\":1,\"price\":299}],\"total\":9427}]},{\"id\":3,\"name\":\"王五\",\"email\":\"wangwu@example.com\",\"address\":{\"street\":\"解放路789号\",\"city\":\"广州\",\"zipcode\":\"510000\",\"coordinates\":{\"latitude\":23.1291,\"longitude\":113.2644}},\"orders\":[{\"orderId\":\"ORD-1123\",\"date\":\"2023-01-05\",\"items\":[{\"productId\":\"P301\",\"name\":\"平板电脑\",\"quantity\":1,\"price\":3299},{\"productId\":\"P302\",\"name\":\"保护套\",\"quantity\":1,\"price\":89}],\"total\":3388},{\"orderId\":\"ORD-1156\",\"date\":\"2023-03-18\",\"items\":[{\"productId\":\"P401\",\"name\":\"智能手表\",\"quantity\":1,\"price\":1299},{\"productId\":\"P402\",\"name\":\"表带\",\"quantity\":2,\"price\":59}],\"total\":1417},{\"orderId\":\"ORD-1198\",\"date\":\"2023-04-01\",\"items\":[{\"productId\":\"P501\",\"name\":\"蓝牙音箱\",\"quantity\":1,\"price\":499},{\"productId\":\"P502\",\"name\":\"音频线\",\"quantity\":1,\"price\":29}],\"total\":528}]}],\"products\":[{\"id\":\"P001\",\"name\":\"智能手机\",\"category\":\"电子产品\",\"price\":5999,\"stock\":150,\"specs\":{\"brand\":\"华为\",\"model\":\"Mate 50\",\"color\":[\"黑色\",\"银色\",\"金色\"],\"storage\":[\"128GB\",\"256GB\",\"512GB\"]}},{\"id\":\"P045\",\"name\":\"手机壳\",\"category\":\"配件\",\"price\":49,\"stock\":500,\"specs\":{\"material\":\"硅胶\",\"compatibility\":[\"Mate 50\",\"iPhone 14\"],\"color\":[\"透明\",\"黑色\",\"蓝色\",\"红色\"]}},{\"id\":\"P112\",\"name\":\"无线耳机\",\"category\":\"电子产品\",\"price\":399,\"stock\":200,\"specs\":{\"brand\":\"小米\",\"model\":\"Redmi Buds 3\",\"color\":[\"白色\",\"黑色\"],\"batteryLife\":\"20小时\"}},{\"id\":\"P087\",\"name\":\"充电宝\",\"category\":\"配件\",\"price\":199,\"stock\":180,\"specs\":{\"capacity\":\"10000mAh\",\"input\":\"5V/2A\",\"output\":\"5V/2.1A\",\"weight\":\"220g\"}},{\"id\":\"P201\",\"name\":\"笔记本电脑\",\"category\":\"电子产品\",\"price\":8999,\"stock\":75,\"specs\":{\"brand\":\"联想\",\"model\":\"ThinkPad X1 Carbon\",\"processor\":\"i7-1165G7\",\"ram\":\"16GB\",\"storage\":\"512GB SSD\",\"display\":\"14英寸 2K\"}},{\"id\":\"P045\",\"name\":\"鼠标\",\"category\":\"外设\",\"price\":129,\"stock\":300,\"specs\":{\"type\":\"无线\",\"dpi\":\"2400\",\"buttons\":6,\"color\":[\"黑色\",\"银色\"]}},{\"id\":\"P046\",\"name\":\"键盘\",\"category\":\"外设\",\"price\":299,\"stock\":120,\"specs\":{\"type\":\"机械键盘\",\"switch\":\"红轴\",\"layout\":\"US QWERTY\",\"backlight\":\"RGB\"}},{\"id\":\"P301\",\"name\":\"平板电脑\",\"category\":\"电子产品\",\"price\":3299,\"stock\":90,\"specs\":{\"brand\":\"苹果\",\"model\":\"iPad Air\",\"storage\":\"64GB\",\"display\":\"10.9英寸\",\"color\":[\"太空灰\",\"粉色\",\"蓝色\"]}},{\"id\":\"P302\",\"name\":\"保护套\",\"category\":\"配件\",\"price\":89,\"stock\":250,\"specs\":{\"compatibility\":\"iPad Air\",\"material\":\"PU皮革\",\"color\":[\"黑色\",\"棕色\",\"蓝色\"]}},{\"id\":\"P401\",\"name\":\"智能手表\",\"category\":\"可穿戴设备\",\"price\":1299,\"stock\":110,\"specs\":{\"brand\":\"华为\",\"model\":\"Watch GT 3\",\"display\":\"1.43英寸 AMOLED\",\"batteryLife\":\"14天\",\"features\":[\"心率监测\",\"血氧检测\",\"GPS\"]}},{\"id\":\"P402\",\"name\":\"表带\",\"category\":\"配件\",\"price\":59,\"stock\":400,\"specs\":{\"compatibility\":\"Watch GT 3\",\"material\":[\"硅胶\",\"不锈钢\",\"皮革\"],\"color\":[\"黑色\",\"银色\",\"棕色\"]}},{\"id\":\"P501\",\"name\":\"蓝牙音箱\",\"category\":\"音频设备\",\"price\":499,\"stock\":85,\"specs\":{\"brand\":\"JBL\",\"model\":\"Flip 5\",\"waterproof\":\"IPX7\",\"batteryLife\":\"12小时\",\"color\":[\"黑色\",\"蓝色\",\"红色\"]}},{\"id\":\"P502\",\"name\":\"音频线\",\"category\":\"配件\",\"price\":29,\"stock\":600,\"specs\":{\"length\":\"1.2m\",\"connectors\":\"3.5mm to 3.5mm\",\"color\":\"黑色\"}}],\"statistics\":{\"totalUsers\":3,\"totalOrders\":6,\"totalProducts\":12,\"totalRevenue\":21455,\"averageOrderValue\":3575.83,\"popularCategories\":[\"电子产品\",\"配件\",\"外设\"],\"monthlySales\":{\"2023-01\":9485,\"2023-02\":9427,\"2023-03\":2015,\"2023-04\":528}},\"settings\":{\"currency\":\"CNY\",\"language\":\"zh-CN\",\"timezone\":\"Asia/Shanghai\",\"featuresEnabled\":{\"multiLanguage\":true,\"darkMode\":true,\"notifications\":true,\"analytics\":false},\"apiLimits\":{\"requestsPerMinute\":1000,\"maxResponseSize\":\"10MB\",\"rateLimitWindow\":\"60s\"}},\"additionalData\":{\"loremIpsum\":\"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam auctor, nisl eget ultricies tincidunt, nisl nisl aliquam nisl, eget ultricies nisl nisl eget nisl. Nullam auctor, nisl eget ultricies tincidunt, nisl nisl aliquam nisl, eget ultricies nisl nisl eget nisl.\",\"randomNumbers\":[42,17,93,56,28,74,31,89,63,15,72,39,84,21,67],\"nestedArrays\":[[1,2,3],[\"a\",\"b\",\"c\"],[true,false,null]],\"deeplyNested\":{\"level1\":{\"level2\":{\"level3\":{\"level4\":{\"level5\":{\"value\":\"最终值\"}}}}}},\"mixedTypes\":{\"string\":\"这是一个字符串\",\"number\":12345,\"boolean\":true,\"nullValue\":null,\"array\":[1,\"two\",false,null],\"object\":{\"key\":\"value\"}}},\"timestamp\":\"2023-05-15T15:45:30.123Z\",\"isActive\":true,\"versionHistory\":[{\"version\":\"0.1.0\",\"date\":\"2023-01-10\",\"changes\":\"初始版本\"},{\"version\":\"0.5.0\",\"date\":\"2023-03-15\",\"changes\":\"添加用户数据\"},{\"version\":\"0.9.0\",\"date\":\"2023-04-20\",\"changes\":\"添加产品数据\"},{\"version\":\"1.0.0\",\"date\":\"2023-05-15\",\"changes\":\"正式发布\"}]}"
        // 打印长文本，自动分段
        LogX.d(longText)

        val json = "{\"key\": \"value\", \"array\":[\"item1\",\"item2\"]}"
        // 打印格式化后的json
        LogX.d(FormatUtils.formatJson(json))

        val xml = "<root><key>value</key><array><item>item1</item><item>item2</item></array></root>"
        // 打印格式化后的xml
        LogX.d(FormatUtils.formatXml(xml))
        LogX.d(FormatUtils.formatXml(xml))

    }

}
