package net.termer.vertx.kotlin.validation.validator

import io.vertx.core.json.JsonArray
import net.termer.vertx.kotlin.validation.ParamValidator
import net.termer.vertx.kotlin.validation.fake.FakeRoutingContext
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * Tests for JsonArrayValidator
 * @author temrer
 */
class JsonArrayValidatorTest {
	@Test
	fun `Parsed validation result is a JsonArray`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "[5, \"test\", 1.0, [], null]", r)
		val validator = JsonArrayValidator()

		val res = validator.validate(param)

		if(!res.valid)
			println(res.errorText)

		assert(res.valid)
		val expected = JsonArray()
				.add(5)
				.add("test")
				.add(1.0)
				.add(JsonArray())
				.add(null)
		assertEquals(res.result, expected)
	}

	@Test
	fun `Report length less than minimum`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "[0]", r)
		val validator = JsonArrayValidator()
				.minLength(2)

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Report length more than maximum`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "[0, 1, 2]", r)
		val validator = JsonArrayValidator()
				.maxLength(2)

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Report invalid types`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "[0, \"str\"]", r)
		val validator = JsonArrayValidator()
				.onlyAllowTypes(arrayOf(Integer::class.java))

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Report nulls present`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "[0, null, 2]", r)
		val validator = JsonArrayValidator()
				.requireNoNulls()

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Validate sub-arrays`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "[[0], []]", r)
		val validator = JsonArrayValidator()
				.validateSubArraysWith(JsonArrayValidator()
						.minLength(1))

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Validate sub-objects`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "[{\"not_key\":\"value\"}]", r)
		val validator = JsonArrayValidator()
				.validateSubObjectsWith(JsonValidator()
						.requireField("key"))

		val res = validator.validate(param)

		assert(!res.valid)
	}
}