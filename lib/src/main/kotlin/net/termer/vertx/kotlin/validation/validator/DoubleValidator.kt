package net.termer.vertx.kotlin.validation.validator

import net.termer.vertx.kotlin.validation.ParamValidator
import net.termer.vertx.kotlin.validation.RequestValidationError

/**
 * Validator for Double parameters
 * @author termer
 * @since 1.0.0
 */
open class DoubleValidator: NumberValidator<Double, DoubleValidator>() {
	override fun validate(param: ParamValidator.Param): ParamValidator.ValidatorResponse {
		var double = try {
			param.value.toDouble()
		} catch(e: NumberFormatException) {
			return ParamValidator.ValidatorResponse(RequestValidationError.DefaultType.INVALID_DOUBLE, "The provided value does not represent a double")
		}

		// Size checks
		if(min != null && double < min!!)
			return ParamValidator.ValidatorResponse(RequestValidationError.DefaultType.INVALID_SIZE, "The provided double is too small (minimum size is $min)")
		if(max != null && double > max!!)
			return ParamValidator.ValidatorResponse(RequestValidationError.DefaultType.INVALID_SIZE, "The provided double is too large (maximum size is $max)")

		// Coercion
		if(coerceMin != null)
			double = double.coerceAtLeast(coerceMin!!)
		if(coerceMax != null)
			double = double.coerceAtMost(coerceMax!!)

		return ParamValidator.ValidatorResponse(double)
	}
}