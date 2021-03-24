package net.termer.vertx.kotlin.validation.validator

import net.termer.vertx.kotlin.validation.ParamValidator
import net.termer.vertx.kotlin.validation.fake.FakeRoutingContext
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * Tests for DoubleValidator
 * @author termer
 */
class DoubleValidatorTest {
	@Test
	fun `Parsed validation result is a double`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "1.5", r)
		val validator = DoubleValidator()

		val res = validator.validate(param)

		if(!res.valid)
			println(res.errorText)

		assert(res.valid)
		val expected = 1.5
		assertEquals(res.result, expected)
	}

	@Test
	fun `Report invalid doubles`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "This isn't a double", r)
		val validator = DoubleValidator()

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Report double less than minimum`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "-2.2", r)
		val validator = DoubleValidator()
				.min(-2.1)

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Report double more than maximum`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "2.2", r)
		val validator = DoubleValidator()
				.max(2.1)

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Coerce double to a minimum`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "-2.2", r)
		val validator = DoubleValidator()
				.coerceMin(-2.1)

		val res = validator.validate(param)

		if(!res.valid)
			println(res.errorText)

		assert(res.valid)
		val expected = -2.1
		assertEquals(res.result, expected)
	}

	@Test
	fun `Coerce double to a maximum`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "2.2", r)
		val validator = DoubleValidator()
				.coerceMax(2.1)

		val res = validator.validate(param)

		if(!res.valid)
			println(res.errorText)

		assert(res.valid)
		val expected = 2.1
		assertEquals(res.result, expected)
	}
}