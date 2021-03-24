package net.termer.vertx.kotlin.validation.validator

import net.termer.vertx.kotlin.validation.ParamValidator
import net.termer.vertx.kotlin.validation.fake.FakeRoutingContext
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * Tests for LongValidator
 * @author termer
 */
class LongValidatorTest {
	@Test
	fun `Parsed validation result is a long`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "5", r)
		val validator = LongValidator()

		val res = validator.validate(param)

		if(!res.valid)
			println(res.errorText)

		assert(res.valid)
		val expected = 5L
		assertEquals(res.result, expected)
	}

	@Test
	fun `Report invalid longs`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "This isn't a long", r)
		val validator = LongValidator()

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Report long less than minimum`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "-1", r)
		val validator = LongValidator()
				.min(0L)

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Report long more than maximum`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "1", r)
		val validator = LongValidator()
				.max(0L)

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Coerce long to a minimum`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "-1", r)
		val validator = LongValidator()
				.coerceMin(0L)

		val res = validator.validate(param)

		if(!res.valid)
			println(res.errorText)

		assert(res.valid)
		val expected = 0L
		assertEquals(res.result, expected)
	}

	@Test
	fun `Coerce long to a maximum`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "1", r)
		val validator = LongValidator()
				.coerceMax(0L)

		val res = validator.validate(param)

		if(!res.valid)
			println(res.errorText)

		assert(res.valid)
		val expected = 0L
		assertEquals(res.result, expected)
	}
}