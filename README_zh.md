# vertx-web-validator-kotlin
一个简单让你写安全空类型Vert.x Web请求验证代码的程序库

# English
You can read the English guide [here](README.md).

# 介绍
Vert.x Web是一个强大的网络开发框架，但是除了最近，没有请求验证程序库。Vert.x 4介绍了一个基于方案的请求验证程序库，但是没有fluent API简单。
这库努力提供一个**简单的**，**可扩展的**，**fluent**，与**空类型安全的**请求验证API。这个API很容易集成到任何现有的用Kotlin的Vert.x Web项目中。
这库努力让你享受NodeJS世界的[express-validator](https://express-validator.github.io/docs/)类型的简单在你的Vert.x Web项目。

# 设定
用你最爱的工具导入程序库。以下是一个Gradle例子：

```groovy
implementation 'net.termer.vertx.kotlin.validation:vertx-web-validator-kotlin:2.0.0'
```

这是请求处理程序例子。接受使用者资料：

```kotlin
// 请求验证
val v = RequestValidator()
		.param("name", StringValidator() // 名字
				.noNewlinesOrControlChars() // 没有新行符号（\n）或者（\r）
				.maxLength(16)) // 最长长度是16字
		.param("age", IntValidator() // 年龄
				.min(13) // 最小是13岁
				.max(99)) // 最大是99岁
		.optionalParam("email", EmailValidator()) // 电子邮件
		.optionalParam("favoriteFood", StringValidator() // 最喜欢的食物
				.minLength(3)
				.maxLength(16), "没有") // 如果请求没有“favoriteFood”参数就用“没有”

if(v.validate(routingContext)) {
	// 收集解析参数
	val name = v.parsedParam("name") as String // 因为我们知道“name”是必须的，我们安全可以把“name”当无空类型。注意“as String”（一直不是空类型）而不是“as String?”（可能包含空类型）。
	val age = v.parsedParam("age") as Int // 因为是必须的，一定没包含空类型
	val email = v.parsedParam("email") as String? // 这个参数可能包含空类型因为是可选的，所以我们应该写“String?”
	val favoriteFood = v.parsedParam("favoriteFood") as String // “favoriteFood”是可选的，但是这个参数一定没包含空类型因为我们以上提供默认值，所以无法包含空类型。

	// 发送响应
	val res = routingContext.response()

	res.send("你的名字是$name\n")
	res.send("你$age岁\n")
	if(email != null)
		res.send("你的电子邮件地址是$email\n")
	res.send("你的最喜欢的食物是$favoriteFood")

	res.end()
} else {
	routingContext.response().end("${v.validationErrorParam}: ${v.validationErrorText}")
}
```

是非常简单，也不需要特别特殊的路由代码。可以安全的用在正常的请求处理程序跟middleware。

# 如何建立自己的验证器

有很多内置验证类型。它们处理数字，字符串，电子邮件，日期，与JSON，但是你也可以建立自己的验证器。建立自己的验证器很简单做因为所有验证器实施ParamValidater interface。在中心，ParamValidator验证字符串，就发送响应。验证器发送验证结果，也可以发送解析值。

这是另一个验证器例子。这个验证器需要接受的字符串以大写字母开头和结尾：

```kotlin
class UppercaseStartAndEndValidator: ParamValidator {
	override fun validate(param: ParamValidator.Param): ParamValidator.ValidatorResponse {
		val str = param.value

		// 确保字符串不是空白的
		return if(str.isBlank()) {
			ParamValidator.ValidatorResponse("BLANK_STRING", "字符串不应该空白的")
		} else {
			// 确保字符串大学字母开头和结尾
			val first = str[0]
			val last = str[str.length - 1]

			if(first.isUpperCase() && last.isUpperCase()) {
				// 按原样返回字符串
				ParamValidator.ValidatorResponse(str)
			} else {
				ParamValidator.ValidatorResponse("START_AND_END_NOT_UPPERCASE", "字符串应该以大写字母开头和结尾")
			}
		}
	}
}
```

你现在可以把你的自己建立的验证器当任何内置验证器。

这是解析逗号分隔列表例子：

```kotlin
class CommaSeparatedArrayValidator: ParamValidator {
	override fun validate(param: ParamValidator.Param): ParamValidator.ValidatorResponse {
		val str = param.value

		// 如果时空白的，就返回空白的数组
		return if(str.isBlank()) {
			ParamValidator.ValidatorResponse(arrayOf<String>())
		} else {
			// 在每个逗号处拆分字符串
			val split = str.split(Regex("[\\W]*,[\\W]*"))
            
            // 返回字符串数组
            ParamValidator.ValidatorResponse(split)
		}
	}
}
```

现在，当你取得参数的解析值，你就取得字符串数组

# 单证
KDoc可在[这里](https://termer.net/kdoc/vertx-web-validator-kotlin/2.0.0/index.html)和Maven Central获得
javadoc可在[这里](https://termer.net/javadoc/vertx-web-validator-kotlin/2.0.0/index.html)和Maven Central获得.

# 许可证
这项目使用MIT许可证，所以你可以在任何项目中自由使用它。

# 联系作者
我的联系方式在[这里](https://termer.net/who/zh.html)。