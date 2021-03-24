package net.termer.vertx.kotlin.validation.validator

import net.termer.vertx.kotlin.validation.ParamValidator
import net.termer.vertx.kotlin.validation.fake.FakeRoutingContext
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * Tests for StringValidator
 * @author termer
 */
class StringValidatorTest {
	@Test
	fun `Parsed validation result is a String`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "Hello world", r)
		val validator = StringValidator()

		val res = validator.validate(param)

		if(!res.valid)
			println(res.errorText)

		assert(res.valid)
		val expected = "Hello world"
		assertEquals(res.result, expected)
	}

	@Test
	fun `String trimming`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "   \n \t Hello world  \n\t\t", r)
		val validator = StringValidator()
				.trim()

		val res = validator.validate(param)

		if(!res.valid)
			println(res.errorText)

		assert(res.valid)
		val expected = "Hello world"
		assertEquals(res.result, expected)
	}

	@Test
	fun `String to lowercase`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "Hello world", r)
		val validator = StringValidator()
				.toLowerCase()

		val res = validator.validate(param)

		if(!res.valid)
			println(res.errorText)

		assert(res.valid)
		val expected = "hello world"
		assertEquals(res.result, expected)
	}

	@Test
	fun `String to uppercase`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "Hello world", r)
		val validator = StringValidator()
				.toUpperCase()

		val res = validator.validate(param)

		if(!res.valid)
			println(res.errorText)

		assert(res.valid)
		val expected = "HELLO WORLD"
		assertEquals(res.result, expected)
	}

	@Test
	fun `Report length shorter than minimum`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "Hello world", r)
		val validator = StringValidator()
				.minLength(12)

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Report length longer than maximum`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "Hello world", r)
		val validator = StringValidator()
				.maxLength(10)

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Report String with characters outside of required list`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "Hello world!", r)
		val validator = StringValidator()
				.requiredCharacters(StringValidator.CharacterLists.lettersNumbersSpaces)

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Report String with characters inside of denied list`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "Hello world", r)
		val validator = StringValidator()
				.denyCharacters(arrayOf('h', 'H'))

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Report lowercase characters when requiring uppercase`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "HELLO WoRLD", r)
		val validator = StringValidator()
				.requireUppercase()

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Report uppercase characters when requiring lowercase`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "Hello world", r)
		val validator = StringValidator()
				.requireLowercase()

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Report not matching provided regex`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "hello world!", r)
		val validator = StringValidator()
				.regex(Regex("^[A-Z][a-z ]+!$")) // "Hello world!" would match this, but the parameter given is missing the first capitalized letter

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Report blank String`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "  \t\n\r     \n   \t   ", r)
		val validator = StringValidator()
				.notBlank()

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Report newlines or control characters`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "Hello\nworld", r)
		val validator = StringValidator()
				.noNewlinesOrControlChars()

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Report String not in array of valid values`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "orange", r)
		val validator = StringValidator()
				.isInArray(arrayOf("apple", "banana", "strawberry"))

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Report String in array of invalid values`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "cookie", r)
		val validator = StringValidator()
				.isNotInArray(arrayOf("cake", "cookie", "tangerine"))

		val res = validator.validate(param)

		assert(!res.valid)
	}
}