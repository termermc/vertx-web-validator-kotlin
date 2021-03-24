package net.termer.vertx.kotlin.validation.validator

import net.termer.vertx.kotlin.validation.ParamValidator

/**
 * Validator for Boolean parameters.
 * Case insensitive, and matches both TRUE/FALSE and ON/OFF.
 * @author termer
 * @since 1.0.0
 */
open class BooleanValidator: ParamValidator {
	private var acceptInvalidAsFalse = false

	override fun validate(param: ParamValidator.Param): ParamValidator.ValidatorResponse {
		return when(param.value.toUpperCase()) {
			"TRUE" -> ParamValidator.ValidatorResponse(true)
			"FALSE" -> ParamValidator.ValidatorResponse(false)
			"ON" -> ParamValidator.ValidatorResponse(true)
			"OFF" -> ParamValidator.ValidatorResponse(false)
			else -> if(acceptInvalidAsFalse)
				ParamValidator.ValidatorResponse(false)
			else
				ParamValidator.ValidatorResponse("INVALID_BOOL", "The provided value does not represent a boolean")
		}
	}

	/**
	 * Accept invalid values and resolve them to false instead of reporting them as invalid
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun acceptInvalidAsFalse(): BooleanValidator {
		acceptInvalidAsFalse = true
		return this
	}
}