# vertx-web-validator-kotlin
A simple Kotlin library for null-safe Vert.x Web request validation

# Intro
Vert.x Web is a powerful framework for web development, but up until recently, it lacked a request validator.
In Vert.x 4, a scheme-based validator was released, but it is not nearly as simple as a fluent API for validation.
This library aims to provide a **simple**, **extensible**, **fluent**, and **null-safe** API for request validation, that is easy to integrate into *any* Vert.x Web project using Kotlin.
It aims to provide the simplicity of libraries like [express-validator](https://express-validator.github.io/docs/) in the NodeJS world.

# Getting Started
Include the library using your favorite build tool. Here is a snippet for Gradle:

```groovy
implementation 'net.termer.vertx.kotlin.validation:vertx-web-validator-kotlin:1.0.1'
```

Here is an example of what you might put inside a request handler that takes in information about a person:

```kotlin
// Request validation
val v = RequestValidator()
		.param("name", StringValidator()
				.noNewlinesOrControlChars()
				.maxLength(16))
        .param("age", IntValidator()
                .min(13)
                .max(99))
        .optionalParam("email", EmailValidator())
		.optionalParam("favoriteFood", StringValidator()
                .minLength(3)
                .maxLength(16, "unspecified")

if(v.validate(routingContext)) {
	// Collect parsed parameters
	val name = v.parsedParam("name") as String // A non-nullable string, since we know for sure that "name" is present
    val age = v.parsedParam("age") as Int // A non-nullable int, since we know for sure that "age" is an integer, and is present
    val email = v.parsedParam("email") as String? // A nullable string, since we're not certain whether email was provided
    val favoriteFood = v.parsedParam("favoriteFood") as String // A non-nullable string, since it is present, or a default value was used
    
    // Send response
    val res = routingContext.response()
    
    res.send("Your name is $name\n")
    res.send("Your age is $age\n")
    if(email != null)
    	res.send("Your email is $email\n")
    res.send("Your favorite food is $favoriteFood")
    
    res.end()
} else {
	routingContext.response().end("${v.validationErrorParam}: ${v.validationErrorText}")
}
```

It's very simple and does not require any special routing. It is safe to use in normal routes and middleware.

# Creating your own validators

There are many built-in validators that handle numbers, strings, emails, dates, and JSON, but you are also free to create your own validators for your own specific use-cases.
This is trivial to do, because all validators implement the ParamValidator interface, which at its most basic level validates a string and sends a status back, optionally parsing the provided string into another type of value.

Here is an example of a validator that makes sure a string ends with and starts with an uppercase letter:

```kotlin
class UppercaseStartAndEndValidator: ParamValidator {
	override fun validate(param: ParamValidator.Param): ParamValidator.ValidatorResponse {
		val str = param.value

		// Make sure string is not blank
		return if(str.isBlank()) {
			ParamValidator.ValidatorResponse("BLANK_STRING", "The provided string is blank")
		} else {
			// Make sure first and last character are both uppercase
			val first = str[0]
			val last = str[str.length - 1]

			if(first.isUpperCase() && last.isUpperCase()) {
				// Return the string as-is
				ParamValidator.ValidatorResponse(str)
			} else {
				ParamValidator.ValidatorResponse("START_AND_END_NOT_UPPERCASE", "The provided string's first and last characters are not uppercase")
			}
		}
	}
}
```

Now you can use this validator like any other.

Here is an example of a validator that parses a comma-separated list into an array:

```kotlin
class CommaSeparatedArrayValidator: ParamValidator {
	override fun validate(param: ParamValidator.Param): ParamValidator.ValidatorResponse {
		val str = param.value

		// Return an empty array if blank
		return if(str.isBlank()) {
			ParamValidator.ValidatorResponse(arrayOf<String>())
		} else {
			// Split the string by comma (with regex to strip whitespace)
			val split = str.split(Regex("[\\W]*,[\\W]*"))
            
            // Return array of strings
            ParamValidator.ValidatorResponse(split)
		}
	}
}
```

Now when you get the parsed value of this parameter, you will get an array of strings.

# Documentation
The KDoc is available [here](https://termer.net/kdoc/vertx-web-validator-kotlin/1.0.1/index.html).
The javadoc is available [here](https://termer.net/javadoc/vertx-web-validator-kotlin/1.0.1/index.html) and on Maven Central.

# License
This project is licensed under the MIT license, so it is freely usable in any project.