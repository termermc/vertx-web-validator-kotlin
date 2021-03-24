package net.termer.vertx.kotlin.validation.validator

import net.termer.vertx.kotlin.validation.ParamValidator

/**
 * Validator for Float parameters
 * @author termer
 * @since 1.0.0
 */
open class FloatValidator: NumberValidator<Float, FloatValidator> {
	private var min: Float? = null
	private var max: Float? = null
	private var coerceMin: Float? = null
	private var coerceMax: Float? = null

	override fun validate(param: ParamValidator.Param): ParamValidator.ValidatorResponse {
		try {
			var float = param.value.toFloat()

			// Size checks
			if(min != null && float < min!!)
				return ParamValidator.ValidatorResponse("INVALID_SIZE", "The provided float is too small (minimum size is $min)")
			if(max != null && float > max!!)
				return ParamValidator.ValidatorResponse("INVALID_SIZE", "The provided float is too large (maximum size is $max)")

			// Coercion
			if(coerceMin != null)
				float = float.coerceAtLeast(coerceMin!!)
			if(coerceMax != null)
				float = float.coerceAtMost(coerceMax!!)

			return ParamValidator.ValidatorResponse(float)
		} catch(e: NumberFormatException) {
			return ParamValidator.ValidatorResponse("INVALID_FLOAT", "The provided value does not represent a float")
		}
	}

	override fun min(size: Float): FloatValidator {
		min = size
		return this
	}
	override fun max(size: Float): FloatValidator {
		max = size
		return this
	}

	override fun coerceMin(size: Float): FloatValidator {
		coerceMin = size
		return this
	}
	override fun coerceMax(size: Float): FloatValidator {
		coerceMax = size
		return this
	}
}