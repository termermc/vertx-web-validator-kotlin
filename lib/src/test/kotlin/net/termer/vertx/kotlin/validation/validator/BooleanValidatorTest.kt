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
			println(res.errorMessage)

		assert(res.valid)
		assertEquals(res.result, true)
	}

	@Test
	fun `Handle ON value`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "on", r)
		val validator = BooleanValidator()

		val res = validator.validate(param)

		assert(res.valid)
		assertEquals(res.result, true)
	}

	@Test
	fun `Reject invalid values`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "something that isn't a boolean", r)
		val validator = BooleanValidator()

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Accept invalid values with acceptInvalidAsFalse method`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "something that isn't a boolean", r)
		val validator = BooleanValidator()
				.acceptInvalidAsFalse()

		val res = validator.validate(param)

		assert(res.valid)
		assertEquals(res.result, false)
	}

	@Test
	fun `Accept custom match strings`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "yes", r)
		val validator = BooleanValidator()
			.addMatchString("yes", true)

		val res = validator.validate(param)

		assertEquals(res.result, true)
		assert(res.valid)
	}

	@Test
	fun `Match string override clears default match strings`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "true", r)
		val validator = BooleanValidator()
			.overrideMatchStrings(hashMapOf(
				"yes" to true,
				"no" to false
			))

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Match strings and values are case-insensitive`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "yES", r)
		val validator = BooleanValidator()
			.addMatchString("Yes", true)

		val res = validator.validate(param)

		assertEquals(res.result, true)
		assert(res.valid)
	}
}