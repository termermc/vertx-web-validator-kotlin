package net.termer.vertx.kotlin.validation.validator

import net.termer.vertx.kotlin.validation.ParamValidator

/**
 * Validator for Double parameters
 * @author termer
 * @since 1.0.0
 */
open class DoubleValidator: NumberValidator<Double, DoubleValidator> {
	private var min: Double? = null
	private var max: Double? = null
	private var coerceMin: Double? = null
	private var coerceMax: Double? = null

	override fun validate(param: ParamValidator.Param): ParamValidator.ValidatorResponse {
		try {
			var double = param.value.toDouble()

			// Size checks
			if(min != null && double < min!!)
				return ParamValidator.ValidatorResponse("INVALID_SIZE", "The provided double is too small (minimum size is $min)")
			if(max != null && double > max!!)
				return ParamValidator.ValidatorResponse("INVALID_SIZE", "The provided double is too large (maximum size is $max)")

			// Coercion
			if(coerceMin != null)
				double = double.coerceAtLeast(coerceMin!!)
			if(coerceMax != null)
				double = double.coerceAtMost(coerceMax!!)

			return ParamValidator.ValidatorResponse(double)
		} catch(e: NumberFormatException) {
			return ParamValidator.ValidatorResponse("INVALID_DOUBLE", "The provided value does not represent a double")
		}
	}

	override fun min(size: Double): DoubleValidator {
		min = size
		return this
	}
	override fun max(size: Double): DoubleValidator {
		max = size
		return this
	}

	override fun coerceMin(size: Double): DoubleValidator {
		coerceMin = size
		return this
	}
	override fun coerceMax(size: Double): DoubleValidator {
		coerceMax = size
		return this
	}
}