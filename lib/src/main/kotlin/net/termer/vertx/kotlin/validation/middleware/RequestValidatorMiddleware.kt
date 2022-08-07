package net.termer.vertx.kotlin.validation.middleware

import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext
import net.termer.vertx.kotlin.validation.RequestValidator
import net.termer.vertx.kotlin.validation.exception.RequestValidationException

/**
 * Middleware that fails with a 400 status code and [RequestValidationException] if the request failed validation
 * @param validator The request validator to use
 * @since 2.0.0
 */
class RequestValidatorMiddleware(val validator: RequestValidator): Handler<RoutingContext> {
	override fun handle(ctx: RoutingContext) {
		try {
			validator.validateOrThrowException(ctx)
			ctx.next()
		} catch(e: RequestValidationException) {
			ctx.fail(400, e)
		}
	}
}