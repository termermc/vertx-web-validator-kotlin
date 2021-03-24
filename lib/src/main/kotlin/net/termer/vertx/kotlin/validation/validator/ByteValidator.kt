package net.termer.vertx.kotlin.validation.validator

import net.termer.vertx.kotlin.validation.ParamValidator

/**
 * Validator for Byte parameters.
 * By default, inputs are parsed as SIGNED bytes.
 * @author termer
 * @since 1.0.0
 */
open class ByteValidator: NumberValidator<Byte, ByteValidator> {
	private var min: Byte? = null
	private var max: Byte? = null
	private var coerceMin: Byte? = null
	private var coerceMax: Byte? = null
	private var asSigned = true

	override fun validate(param: ParamValidator.Param): ParamValidator.ValidatorResponse {
		try {
			// Parse as int at first, to be able to handle either signed or unsigned inputs
			val int = param.value.toInt()

			// Validate based on signed or unsigned range
			if((asSigned && (int < -128 || int > 127)) || (!asSigned && (int < 0 || int > 255)))
				return ParamValidator.ValidatorResponse("INVALID_BYTE", "The provided value does not represent a${if(asSigned) " signed" else "n unsigned"} byte")

			// Handle as either signed or unsigned byte
			var byte = if(asSigned)
				int.toByte()
			else
				(int-128).toByte()

			// Size checks
			if(min != null && byte < min!!)
				return ParamValidator.ValidatorResponse("INVALID_SIZE", "The provided byte is too small (minimum size is $min)")
			if(max != null && byte > max!!)
				return ParamValidator.ValidatorResponse("INVALID_SIZE", "The provided byte is too large (maximum size is $max)")

			// Coercion
			if(coerceMin != null)
				byte = byte.coerceAtLeast(coerceMin!!)
			if(coerceMax != null)
				byte = byte.coerceAtMost(coerceMax!!)

			return ParamValidator.ValidatorResponse(byte)
		} catch(e: NumberFormatException) {
			return ParamValidator.ValidatorResponse("INVALID_BYTE", "The provided value does not represent a byte")
		}
	}

	override fun min(size: Byte): ByteValidator {
		min = size
		return this
	}
	override fun max(size: Byte): ByteValidator {
		max = size
		return this
	}

	override fun coerceMin(size: Byte): ByteValidator {
		coerceMin = size
		return this
	}
	override fun coerceMax(size: Byte): ByteValidator {
		coerceMax = size
		return this
	}

	/**
	 * Parses inputs as signed bytes (-128 to 127).
	 * This is the default.
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun parseAsSigned(): ByteValidator {
		asSigned = true
		return this
	}

	/**
	 * Parses input as unsigned bytes (0 to 255)
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun parseAsUnsigned(): ByteValidator {
		asSigned = false
		return this
	}
}