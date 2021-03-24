package net.termer.vertx.kotlin.validation.validator

import net.termer.vertx.kotlin.validation.ParamValidator
import net.termer.vertx.kotlin.validation.fake.FakeRoutingContext
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * Tests for FloatValidator
 * @author termer
 */
class FloatValidatorTest {
	@Test
	fun `Parsed validation result is a float`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "1.5", r)
		val validator = FloatValidator()

		val res = validator.validate(param)

		if(!res.valid)
			println(res.errorText)

		assert(res.valid)
		val expected = 1.5F
		assertEquals(res.result, expected)
	}

	@Test
	fun `Report invalid floats`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "This isn't a float", r)
		val validator = FloatValidator()

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Report float less than minimum`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "-2.2", r)
		val validator = FloatValidator()
				.min(-2.1F)

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Report float more than maximum`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "2.2", r)
		val validator = FloatValidator()
				.max(2.1F)

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Coerce float to a minimum`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "-2.2", r)
		val validator = FloatValidator()
				.coerceMin(-2.1F)

		val res = validator.validate(param)

		if(!res.valid)
			println(res.errorText)

		assert(res.valid)
		val expected = -2.1F
		assertEquals(res.result, expected)
	}

	@Test
	fun `Coerce float to a maximum`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "2.2", r)
		val validator = FloatValidator()
				.coerceMax(2.1F)

		val res = validator.validate(param)

		if(!res.valid)
			println(res.errorText)

		assert(res.valid)
		val expected = 2.1F
		assertEquals(res.result, expected)
	}
}