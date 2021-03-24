package net.termer.vertx.kotlin.validation.validator

import net.termer.vertx.kotlin.validation.ParamValidator
import net.termer.vertx.kotlin.validation.fake.FakeRoutingContext
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * Tests for ByteValidator
 * @author termer
 */
class ByteValidatorTest {
	@Test
	fun `Parsed validation result is a byte`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "-128", r)
		val validator = ByteValidator()

		val res = validator.validate(param)

		if(!res.valid)
			println(res.errorText)

		assert(res.valid)
		val expected: Byte = -128
		assertEquals(res.result, expected)
	}

	@Test
	fun `Handle unsigned byte inputs`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "0", r)
		val validator = ByteValidator()
				.parseAsUnsigned()

		val res = validator.validate(param)

		if(!res.valid)
			println(res.errorText)

		assert(res.valid)
		val expected: Byte = -128
		assertEquals(res.result, expected)
	}

	@Test
	fun `Report invalid signed bytes`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "255", r)
		val validator = ByteValidator()

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Report invalid unsigned bytes`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "-128", r)
		val validator = ByteValidator()
				.parseAsUnsigned()

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Report byte less than minimum`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "-1", r)
		val validator = ByteValidator()
				.min(0)

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Report byte more than maximum`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "1", r)
		val validator = ByteValidator()
				.max(0)

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Coerce byte to a minimum`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "-128", r)
		val validator = ByteValidator()
				.coerceMin(0)

		val res = validator.validate(param)

		if(!res.valid)
			println(res.errorText)

		assert(res.valid)
		val expected: Byte = 0
		assertEquals(res.result, expected)
	}

	@Test
	fun `Coerce byte to a maximum`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "127", r)
		val validator = ByteValidator()
				.coerceMax(0)

		val res = validator.validate(param)

		if(!res.valid)
			println(res.errorText)

		assert(res.valid)
		val expected: Byte = 0
		assertEquals(res.result, expected)
	}
}