package net.termer.vertx.kotlin.validation.validator

import net.termer.vertx.kotlin.validation.ParamValidator
import net.termer.vertx.kotlin.validation.fake.FakeRoutingContext
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime
import kotlin.test.assertEquals

/**
 * Tests for [OffsetDateTimeValidator]
 * @author termer
 */
class OffsetDateTimeValidatorTest {
	@Test
	fun `Parsed validation result is a OffsetDateTime`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "2021-03-24T01:22:26.400Z", r)
		val validator = OffsetDateTimeValidator()

		val res = validator.validate(param)

		if(!res.valid)
			println(res.errorMessage)

		assert(res.valid)
		val expected = OffsetDateTime.parse("2021-03-24T01:22:26.400Z")
		assertEquals(res.result, expected)
	}

	@Test
	fun `Report time before minimum`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "2021-03-24T01:22:26.400Z", r)
		val validator = OffsetDateTimeValidator()
				.min(OffsetDateTime.parse("2021-03-25T01:46:43.700Z"))

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Report time after maximum`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "2021-03-24T01:22:26.400Z", r)
		val validator = OffsetDateTimeValidator()
				.max(OffsetDateTime.parse("2021-03-23T01:46:43.700Z"))

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Coerce time to minimum`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "2021-03-24T01:22:26.400Z", r)
		val validator = OffsetDateTimeValidator()
				.coerceMin(OffsetDateTime.parse("2021-03-25T01:46:43.700Z"))

		val res = validator.validate(param)

		if(!res.valid)
			println(res.errorMessage)

		assert(res.valid)
		val expected = OffsetDateTime.parse("2021-03-25T01:46:43.700Z")
		assertEquals(res.result, expected)
	}

	@Test
	fun `Coerce time to maximum`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "2021-03-24T01:22:26.400Z", r)
		val validator = OffsetDateTimeValidator()
				.coerceMax(OffsetDateTime.parse("2021-03-23T01:46:43.700Z"))

		val res = validator.validate(param)

		if(!res.valid)
			println(res.errorMessage)

		assert(res.valid)
		val expected = OffsetDateTime.parse("2021-03-23T01:46:43.700Z")
		assertEquals(res.result, expected)
	}
}