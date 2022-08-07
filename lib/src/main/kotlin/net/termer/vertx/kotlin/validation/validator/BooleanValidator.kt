package net.termer.vertx.kotlin.validation.validator

import net.termer.vertx.kotlin.validation.ParamValidator
import net.termer.vertx.kotlin.validation.RequestValidationError

/**
 * Validator for Boolean parameters.
 * Case-insensitive, and matches both "true"/"false" and "on"/"off" by default.
 * Strings matched to boolean values can be overridden with the [overrideMatchStrings] method.
 * @author termer
 * @since 1.0.0
 */
open class BooleanValidator: ParamValidator {
	private var acceptInvalidAsFalse = false
	private var matchStrings = hashMapOf(
		"true" to true,
		"false" to false,
		"on" to true,
		"off" to false
	)

	override fun validate(param: ParamValidator.Param): ParamValidator.ValidatorResponse {
		val valLowercase = param.value.lowercase()

		// Check value against match strings
		for((str, bool) in matchStrings)
			if(valLowercase == str.lowercase())
				return ParamValidator.ValidatorResponse(bool)

		// If nothing matched, the value does not represent a boolean; use false if specified, otherwise return an error response
		return if(acceptInvalidAsFalse)
			ParamValidator.ValidatorResponse(false)
		else
			ParamValidator.ValidatorResponse(RequestValidationError.DefaultType.INVALID_BOOL, "The provided value does not represent a boolean")
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

	/**
	 * Overrides match strings for boolean values.
	 * Use this if you want to completely override the defaults like "true" and "false" to your own values.
	 * If you simply want to add more match strings, use the [addMatchString] method.
	 * @param matchStrings A map of match strings that correspond to boolean values to use (values are copied from the map, the provided map will never be modified or accessed after this call)
	 * @return This, to be used fluently
	 * @since 2.0.0
	 */
	fun overrideMatchStrings(matchStrings: Map<String, Boolean>): BooleanValidator {
		this.matchStrings.clear()
		addMatchStrings(matchStrings)
		return this
	}

	/**
	 * Adds multiple match strings for boolean values from a Map.
	 * @param matchStrings A map of match strings that correspond to boolean values to use (values are copied from the map, the provided map will never be modified or accessed after this call)
	 * @return This, to be used fluently
	 * @since 2.0.0
	 */
	fun addMatchStrings(matchStrings: Map<String, Boolean>): BooleanValidator {
		this.matchStrings.putAll(matchStrings)
		return this
	}

	/**
	 * Adds a match string for a boolean value (e.g. "yes" -> true).
	 * To add multiple values from a map, use the [addMatchStrings] method
	 * @param matchString The match string
	 * @param bool The boolean value that it corresponds to
	 * @return This, to be used fluently
	 * @since 2.0.0
	 */
	fun addMatchString(matchString: String, bool: Boolean): BooleanValidator {
		matchStrings[matchString] = bool
		return this
	}
}