package net.termer.vertx.kotlin.validation.validator

import net.termer.vertx.kotlin.validation.ParamValidator

/**
 * Validator for Long parameters
 * @author termer
 * @since 1.0.0
 */
class LongValidator: NumberValidator<Long, LongValidator> {
	private var min: Long? = null
	private var max: Long? = null
	private var coerceMin: Long? = null
	private var coerceMax: Long? = null

	override fun validate(param: ParamValidator.Param): ParamValidator.ValidatorResponse {
		try {
			var long = param.value.toLong()

			// Size checks
			if(min != null && long < min!!)
				return ParamValidator.ValidatorResponse("INVALID_SIZE", "The provided long is too small (minimum size is $min)")
			if(max != null && long > max!!)
				return ParamValidator.ValidatorResponse("INVALID_SIZE", "The provided long is too large (maximum size is $max)")

			// Coercion
			if(coerceMin != null)
				long = long.coerceAtLeast(coerceMin!!)
			if(coerceMax != null)
				long = long.coerceAtMost(coerceMax!!)

			return ParamValidator.ValidatorResponse(long)
		} catch(e: NumberFormatException) {
			return ParamValidator.ValidatorResponse("INVALID_LONG", "The provided value does not represent a long")
		}
	}

	override fun min(size: Long): LongValidator {
		min = size
		return this
	}
	override fun max(size: Long): LongValidator {
		max = size
		return this
	}

	override fun coerceMin(size: Long): LongValidator {
		coerceMin = size
		return this
	}
	override fun coerceMax(size: Long): LongValidator {
		coerceMax = size
		return this
	}
}