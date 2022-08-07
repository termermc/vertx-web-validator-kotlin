package net.termer.vertx.kotlin.validation.validator

import net.termer.vertx.kotlin.validation.ParamValidator
import net.termer.vertx.kotlin.validation.RequestValidationError

/**
 * Validator for Float parameters
 * @author termer
 * @since 1.0.0
 */
open class FloatValidator: NumberValidator<Float, FloatValidator>() {
	override fun validate(param: ParamValidator.Param): ParamValidator.ValidatorResponse {
		var float = try {
			param.value.toFloat()
		} catch(e: NumberFormatException) {
			return ParamValidator.ValidatorResponse(RequestValidationError.DefaultType.INVALID_FLOAT, "The provided value does not represent a float")
		}

		// Size checks
		if(min != null && float < min!!)
			return ParamValidator.ValidatorResponse(RequestValidationError.DefaultType.INVALID_SIZE, "The provided float is too small (minimum size is $min)")
		if(max != null && float > max!!)
			return ParamValidator.ValidatorResponse(RequestValidationError.DefaultType.INVALID_SIZE, "The provided float is too large (maximum size is $max)")

		// Coercion
		if(coerceMin != null)
			float = float.coerceAtLeast(coerceMin!!)
		if(coerceMax != null)
			float = float.coerceAtMost(coerceMax!!)

		return ParamValidator.ValidatorResponse(float)
	}
}