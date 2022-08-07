package net.termer.vertx.kotlin.validation.validator

import net.termer.vertx.kotlin.validation.ParamValidator
import net.termer.vertx.kotlin.validation.RequestValidationError

/**
 * Validator for Int parameters
 * @author termer
 * @since 1.0.0
 */
open class IntValidator: NumberValidator<Int, IntValidator>() {
	override fun validate(param: ParamValidator.Param): ParamValidator.ValidatorResponse {
		var integer = try {
			param.value.toInt()
		} catch(e: NumberFormatException) {
			return ParamValidator.ValidatorResponse(RequestValidationError.DefaultType.INVALID_INT, "The provided value does not represent an integer")
		}

		// Size checks
		if(min != null && integer < min!!)
			return ParamValidator.ValidatorResponse(RequestValidationError.DefaultType.INVALID_SIZE, "The provided integer is too small (minimum size is $min)")
		if(max != null && integer > max!!)
			return ParamValidator.ValidatorResponse(RequestValidationError.DefaultType.INVALID_SIZE, "The provided integer is too large (maximum size is $max)")

		// Coercion
		if(coerceMin != null)
			integer = integer.coerceAtLeast(coerceMin!!)
		if(coerceMax != null)
			integer = integer.coerceAtMost(coerceMax!!)

		return ParamValidator.ValidatorResponse(integer)
	}
}