package net.termer.vertx.kotlin.validation.validator

import net.termer.vertx.kotlin.validation.ParamValidator
import net.termer.vertx.kotlin.validation.fake.FakeRoutingContext
import org.junit.jupiter.api.Test

/**
 * Tests for [NoneValidator]
 * @author termer
 */
class NoneValidatorTest {
	@Test
	fun `Does not accept any input`() {
		val r = FakeRoutingContext()

		val param = ParamValidator.Param("param", "", r)
		val validator = NoneValidator()

		val res = validator.validate(param)

		assert(!res.valid)
	}
}