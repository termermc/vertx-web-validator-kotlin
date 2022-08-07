package net.termer.vertx.kotlin.validation.middleware

import net.termer.vertx.kotlin.validation.RequestValidator
import net.termer.vertx.kotlin.validation.fake.FakeRoutingContext
import net.termer.vertx.kotlin.validation.validator.AnyValidator
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Tests for [RequestValidatorMiddleware]
 * @author termer
 */
class RequestValidatorMiddlewareTest {
	@Test
	fun `Pass to next handler on successful validation`() {
		val ctx = FakeRoutingContext()
		ctx.request().params()["param"] = "anything"

		val validator = RequestValidator()
			.param("param", AnyValidator())

		val middleware = RequestValidatorMiddleware(validator)

		middleware.handle(ctx)

		assert(ctx.nextCalled)
	}

	@Test
	fun `Fail with exception on failed validation`() {
		val ctx = FakeRoutingContext()

		val validator = RequestValidator()
			.param("param", AnyValidator())

		val middleware = RequestValidatorMiddleware(validator)

		middleware.handle(ctx)

		assert(!ctx.nextCalled)
		assert(ctx.failCalled)
		assertEquals(ctx.failStatus, 400)
		assertNotNull(ctx.failThrowable)
	}
}