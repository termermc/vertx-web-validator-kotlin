package net.termer.vertx.kotlin.validation.validator

import net.termer.vertx.kotlin.validation.ParamValidator
import net.termer.vertx.kotlin.validation.RequestValidationError

/**
 * Dummy validator that does not accept any input
 * @author termer
 * @since 2.0.0
 */
open class NoneValidator: ParamValidator {
	override fun validate(param: ParamValidator.Param) = ParamValidator.ValidatorResponse(RequestValidationError.DefaultType.INVALID_VALUE, "This parameter cannot possibly be valid")
}