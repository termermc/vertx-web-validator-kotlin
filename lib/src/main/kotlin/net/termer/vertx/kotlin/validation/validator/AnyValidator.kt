package net.termer.vertx.kotlin.validation.validator

import net.termer.vertx.kotlin.validation.ParamValidator

/**
 * Dummy validator that accepts any input
 * @author termer
 * @since 1.0.0
 */
open class AnyValidator: ParamValidator {
	override fun validate(param: ParamValidator.Param) = ParamValidator.ValidatorResponse(param.value)
}