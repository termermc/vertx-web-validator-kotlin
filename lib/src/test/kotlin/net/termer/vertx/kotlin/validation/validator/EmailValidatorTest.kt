package net.termer.vertx.kotlin.validation.validator

import net.termer.vertx.kotlin.validation.ParamValidator
import net.termer.vertx.kotlin.validation.fake.FakeRoutingContext
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * Tests for EmailValidator
 * @author termer
 */
class EmailValidatorTest {
	@Test
	fun `Parsed validation result is a String`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "me@example.com", r)
		val validator = EmailValidator()

		val res = validator.validate(param)

		if(!res.valid)
			println(res.errorText)

		assert(res.valid)
		val expected = "me@example.com"
		assertEquals(res.result, expected)
	}

	@Test
	fun `Email trimming`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "\t me@example.com \n ", r)
		val validator = EmailValidator()
				.trim()

		val res = validator.validate(param)

		if(!res.valid)
			println(res.errorText)

		assert(res.valid)
		val expected = "me@example.com"
		assertEquals(res.result, expected)
	}

	@Test
	fun `Report length shorter than minimum`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "me@example.com", r)
		val validator = EmailValidator()
				.minLength(16)

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Report length longer than maximum`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "me@example.com", r)
		val validator = EmailValidator()
				.maxLength(8)

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Handle subdomains`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "person@mail.example.com", r)
		val validator = EmailValidator()

		val res = validator.validate(param)

		if(!res.valid)
			println(res.errorText)

		assert(res.valid)
	}

	@Test
	fun `Handle long TLD`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "persona@un-sitio-web.tienda", r)
		val validator = EmailValidator()

		val res = validator.validate(param)

		if(!res.valid)
			println(res.errorText)

		assert(res.valid)
	}
}