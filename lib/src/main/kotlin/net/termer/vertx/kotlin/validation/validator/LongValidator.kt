package net.termer.vertx.kotlin.validation.validator

import net.termer.vertx.kotlin.validation.ParamValidator
import net.termer.vertx.kotlin.validation.RequestValidationError

/**
 * Validator for Long parameters
 * @author termer
 * @since 1.0.0
 */
class LongValidator: NumberValidator<Long, LongValidator>() {
	override fun validate(param: ParamValidator.Param): ParamValidator.ValidatorResponse {
		var long = try {
			param.value.toLong()
		} catch(e: NumberFormatException) {
			return ParamValidator.ValidatorResponse(RequestValidationError.DefaultType.INVALID_LONG, "The provided value does not represent a long")
		}

		// Size checks
		if(min != null && long < min!!)
			return ParamValidator.ValidatorResponse(RequestValidationError.DefaultType.INVALID_SIZE, "The provided long is too small (minimum size is $min)")
		if(max != null && long > max!!)
			return ParamValidator.ValidatorResponse(RequestValidationError.DefaultType.INVALID_SIZE, "The provided long is too large (maximum size is $max)")

		// Coercion
		if(coerceMin != null)
			long = long.coerceAtLeast(coerceMin!!)
		if(coerceMax != null)
			long = long.coerceAtMost(coerceMax!!)

		return ParamValidator.ValidatorResponse(long)
	}
}