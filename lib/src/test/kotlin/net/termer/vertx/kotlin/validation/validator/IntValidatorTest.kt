package net.termer.vertx.kotlin.validation.validator

import net.termer.vertx.kotlin.validation.ParamValidator
import net.termer.vertx.kotlin.validation.fake.FakeRoutingContext
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * Tests for [IntValidator]
 * @author termer
 */
class IntValidatorTest {
	@Test
	fun `Parsed validation result is an int`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "5", r)
		val validator = IntValidator()

		val res = validator.validate(param)

		if(!res.valid)
			println(res.errorMessage)

		assert(res.valid)
		val expected = 5
		assertEquals(res.result, expected)
	}

	@Test
	fun `Report invalid ints`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "This isn't an int", r)
		val validator = FloatValidator()

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Report int less than minimum`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "-1", r)
		val validator = IntValidator()
				.min(0)

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Report int more than maximum`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "1", r)
		val validator = IntValidator()
				.max(0)

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Coerce int to a minimum`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "-1", r)
		val validator = IntValidator()
				.coerceMin(0)

		val res = validator.validate(param)

		if(!res.valid)
			println(res.errorMessage)

		assert(res.valid)
		val expected = 0
		assertEquals(res.result, expected)
	}

	@Test
	fun `Coerce int to a maximum`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "1", r)
		val validator = IntValidator()
				.coerceMax(0)

		val res = validator.validate(param)

		if(!res.valid)
			println(res.errorMessage)

		assert(res.valid)
		val expected = 0
		assertEquals(res.result, expected)
	}
}