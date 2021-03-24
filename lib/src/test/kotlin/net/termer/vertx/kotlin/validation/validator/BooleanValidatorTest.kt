package net.termer.vertx.kotlin.validation.validator

import net.termer.vertx.kotlin.validation.ParamValidator
import net.termer.vertx.kotlin.validation.fake.FakeRoutingContext
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * Tests for BooleanValidator
 * @author termer
 */
class BooleanValidatorTest {
	@Test
	fun `Parsed validation result is a boolean`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "true", r)
		val validator = BooleanValidator()

		val res = validator.validate(param)

		if(!res.valid)
			println(res.errorText)

		assert(res.valid)
		assertEquals(res.result, true)
	}

	@Test
	fun `Handle ON value`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "on", r)
		val validator = BooleanValidator()

		val res = validator.validate(param)

		if(!res.valid)
			println(res.errorText)

		assert(res.valid)
		assertEquals(res.result, true)
	}

	@Test
	fun `Reject invalid values`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "something that isn't a boolean", r)
		val validator = BooleanValidator()

		val res = validator.validate(param)

		if(!res.valid)
			println(res.errorText)

		assert(!res.valid)
	}

	@Test
	fun `Accept invalid values with acceptInvalidAsFalse method`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "something that isn't a boolean", r)
		val validator = BooleanValidator()
				.acceptInvalidAsFalse()

		val res = validator.validate(param)

		if(!res.valid)
			println(res.errorText)

		assert(res.valid)
		assertEquals(res.result, false)
	}
}