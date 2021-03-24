package net.termer.vertx.kotlin.validation.validator

import net.termer.vertx.kotlin.validation.ParamValidator
import java.time.OffsetDateTime
import java.time.format.DateTimeParseException

/**
 * Validator for OffsetDateTime (ISO date) parameters
 * @author termer
 * @since 1.0.0
 */
open class OffsetDateTimeValidator: ParamValidator {
	private var min: OffsetDateTime? = null
	private var max: OffsetDateTime? = null
	private var coerceMin: OffsetDateTime? = null
	private var coerceMax: OffsetDateTime? = null

	override fun validate(param: ParamValidator.Param): ParamValidator.ValidatorResponse {
		try {
			var time = OffsetDateTime.parse(param.value)

			if(coerceMin != null && time.isBefore(coerceMin))
				time = coerceMin
			if(coerceMax != null && time.isAfter(coerceMax))
				time = coerceMax
			if(min != null && time.isBefore(min))
				return ParamValidator.ValidatorResponse("INVALID_TIME", "The provided time is before the minimum allowed time ($min)")
			if(max != null && time.isAfter(max))
				return ParamValidator.ValidatorResponse("INVALID_TIME", "The provided time is after the maximum allowed time ($max)")

			return ParamValidator.ValidatorResponse(time)
		} catch(e: DateTimeParseException) {
			return ParamValidator.ValidatorResponse("INVALID_DATE", "The provided value does not represent an ISO date string")
		}
	}

	/**
	 * Requires a minimum time
	 * @param time The required minimum time
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun min(time: OffsetDateTime): OffsetDateTimeValidator {
		min = time
		return this
	}

	/**
	 * Requires a maximum time
	 * @param time The required maximum time
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun max(time: OffsetDateTime): OffsetDateTimeValidator {
		max = time
		return this
	}

	/**
	 * Coerces the time to be at least the provided time
	 * @param time The minimum time to set the value to
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun coerceMin(time: OffsetDateTime): OffsetDateTimeValidator {
		coerceMin = time
		return this
	}
	/**
	 * Coerces the time to be at most the provided time
	 * @param time The maximum time to set the value to
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun coerceMax(time: OffsetDateTime): OffsetDateTimeValidator {
		coerceMax = time
		return this
	}
}