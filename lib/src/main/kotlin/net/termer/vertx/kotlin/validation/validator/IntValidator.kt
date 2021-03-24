package net.termer.vertx.kotlin.validation.validator

import net.termer.vertx.kotlin.validation.ParamValidator

/**
 * Validator for Int parameters
 * @author termer
 * @since 1.0.0
 */
open class IntValidator: NumberValidator<Int, IntValidator> {
	private var min: Int? = null
	private var max: Int? = null
	private var coerceMin: Int? = null
	private var coerceMax: Int? = null

	override fun validate(param: ParamValidator.Param): ParamValidator.ValidatorResponse {
		try {
			var integer = param.value.toInt()

			// Size checks
			if(min != null && integer < min!!)
				return ParamValidator.ValidatorResponse("INVALID_SIZE", "The provided integer is too small (minimum size is $min)")
			if(max != null && integer > max!!)
				return ParamValidator.ValidatorResponse("INVALID_SIZE", "The provided integer is too large (maximum size is $max)")

			// Coercion
			if(coerceMin != null)
				integer = integer.coerceAtLeast(coerceMin!!)
			if(coerceMax != null)
				integer = integer.coerceAtMost(coerceMax!!)

			return ParamValidator.ValidatorResponse(integer)
		} catch(e: NumberFormatException) {
			return ParamValidator.ValidatorResponse("INVALID_INT", "The provided value does not represent an integer")
		}
	}

	override fun min(size: Int): IntValidator {
		min = size
		return this
	}
	override fun max(size: Int): IntValidator {
		max = size
		return this
	}

	override fun coerceMin(size: Int): IntValidator {
		coerceMin = size
		return this
	}
	override fun coerceMax(size: Int): IntValidator {
		coerceMax = size
		return this
	}
}