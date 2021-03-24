package net.termer.vertx.kotlin.validation.validator

import io.vertx.core.json.JsonObject
import net.termer.vertx.kotlin.validation.ParamValidator
import net.termer.vertx.kotlin.validation.fake.FakeRoutingContext
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * Tests for JsonValidator
 * @author termer
 */
class JsonValidatorTest {
	@Test
	fun `Parsed validation result is a JsonObject`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "{\"key\": \"value\", \"key2\": 0}", r)
		val validator = JsonValidator()

		val res = validator.validate(param)

		if(!res.valid)
			println(res.errorText)

		assert(res.valid)
		val expected = JsonObject()
				.put("key", "value")
				.put("key2", 0)
		assertEquals(res.result, expected)
	}

	@Test
	fun `Require field`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "{\"key\": \"value\", \"key2\": 0}", r)
		val validator = JsonValidator()
				.requireField("key3")

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Require field with type`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "{\"key\": \"value\", \"key2\": 0}", r)
		val validator = JsonValidator()
				.requireField("key", Integer::class.java)

		val res = validator.validate(param)

		assert(!res.valid)
	}

	@Test
	fun `Require field with validator`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "{\"key\": []}", r)
		val validator = JsonValidator()
				.validateFieldWith("key", JsonArrayValidator()
						.minLength(1))

		val res = validator.validate(param)

		assert(!res.valid)
	}
}