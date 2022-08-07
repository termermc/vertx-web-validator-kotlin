package net.termer.vertx.kotlin.validation.exception

import io.vertx.ext.web.RoutingContext
import net.termer.vertx.kotlin.validation.RequestValidationError
import net.termer.vertx.kotlin.validation.RequestValidator

/**
 * Exception thrown when request validation fails
 * @param message The exception message
 * @param validator The validator instance that generated this exception
 * @param routingContext The routing context of the request that failed validation
 * @since 2.0.0
 * @author termer
 */
open class RequestValidationException(message: String, val validator: RequestValidator, val routingContext: RoutingContext): Exception(message) {
	/**
	 * All validation errors that were found.
	 * Alias to [RequestValidator.validationErrors] on [validator].
	 * @since 2.0.0
	 */
	val errors: Array<RequestValidationError>
		get() = validator.validationErrors
}