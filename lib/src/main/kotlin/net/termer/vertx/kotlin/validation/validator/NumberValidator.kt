package net.termer.vertx.kotlin.validation.validator

import net.termer.vertx.kotlin.validation.ParamValidator

/**
 * Abstract class for number validators
 * @author termer
 * @since 1.0.0
 */
@Suppress("UNCHECKED_CAST")
abstract class NumberValidator<in T: Number, THIS>: ParamValidator {
	protected var min: @UnsafeVariance T? = null
	protected var max: @UnsafeVariance T? = null
	protected var coerceMin: @UnsafeVariance T? = null
	protected var coerceMax: @UnsafeVariance T? = null

	/**
	 * Requires a minimum size
	 * @param size The required minimum size
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun min(size: T): THIS {
		min = size
		return this as THIS
	}
	/**
	 * Requires a maximum size
	 * @param size The required maximum size
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun max(size: T): THIS {
		max = size
		return this as THIS
	}

	/**
	 * Coerces the number to be at least the provided size
	 * @param size The minimum size to set the value to
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun coerceMin(size: T): THIS {
		coerceMin = size
		return this as THIS
	}
	/**
	 * Coerces the number to be at most the provided size
	 * @param size The maximum size to set the value to
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun coerceMax(size: T): THIS {
		coerceMax = size
		return this as THIS
	}
}