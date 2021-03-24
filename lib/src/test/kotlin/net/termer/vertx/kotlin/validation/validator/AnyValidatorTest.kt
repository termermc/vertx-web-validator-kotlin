package net.termer.vertx.kotlin.validation.validator

import net.termer.vertx.kotlin.validation.ParamValidator
import net.termer.vertx.kotlin.validation.fake.FakeRoutingContext
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * Tests for AnyValidator
 * @author termer
 */
class AnyValidatorTest {
	@Test
	fun `Parsed validation result is the same as input`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "value", r)
		val validator = AnyValidator()

		val res = validator.validate(param)

		assert(res.valid)
		assertEquals(res.result, "value")
	}
}