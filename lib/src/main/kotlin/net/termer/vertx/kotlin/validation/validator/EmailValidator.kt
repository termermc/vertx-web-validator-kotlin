package net.termer.vertx.kotlin.validation.validator

import net.termer.vertx.kotlin.validation.ParamValidator

/**
 * Validator for email parameters
 * @author termer
 * @since 1.0.0
 */
open class EmailValidator: ParamValidator {
	private var trim = false
	private var minLen: Int? = null
	private var maxLen: Int? = null

	override fun validate(param: ParamValidator.Param): ParamValidator.ValidatorResponse {
		var str = param.value

		if(trim)
			str = str.trim()

		// Length checks
		if(minLen != null && str.length < minLen!!)
			return ParamValidator.ValidatorResponse("INVALID_LENGTH", "The provided email is too short (minimum length is $minLen)")
		if(maxLen != null && str.length > maxLen!!)
			return ParamValidator.ValidatorResponse("INVALID_LENGTH", "The provided email is too long (maximum length is $maxLen)")

		// Check if blank
		if(str.isBlank())
			return ParamValidator.ValidatorResponse("BLANK_STRING", "The provided email is blank")

		// Check for regex
		if(!str.matches(Regex("^[\\w.]+@[a-zA-Z_0-9\\-.]+?\\.[a-zA-Z]{2,16}\$")))
			return ParamValidator.ValidatorResponse("INVALID_EMAIL", "The provided email is not valid")

		return ParamValidator.ValidatorResponse(str)
	}

	/**
	 * Trims the String before processing or validating it
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun trim(): EmailValidator {
		trim = true
		return this
	}

	/**
	 * Requires a minimum length
	 * @param length The required minimum length
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun minLength(length: Int): EmailValidator {
		minLen = length
		return this
	}
	/**
	 * Requires a maximum length
	 * @param length The required maximum length
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun maxLength(length: Int): EmailValidator {
		maxLen = length
		return this
	}
}