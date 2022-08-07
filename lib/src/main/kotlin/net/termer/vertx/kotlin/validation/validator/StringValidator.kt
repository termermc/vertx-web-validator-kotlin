package net.termer.vertx.kotlin.validation.validator

import net.termer.vertx.kotlin.validation.ParamValidator
import net.termer.vertx.kotlin.validation.RequestValidationError

/**
 * Validator for String parameters
 * @author termer
 * @since 1.0.0
 */
open class StringValidator: ParamValidator {
	/**
	 * Lists of characters for use with required/denied character methods
	 * @author termer
	 * @since 1.0.0
	 */
	class CharacterLists {
		companion object {
			/**
			 * An array of letters, used for required/denied character lists
			 * @since 1.0.0
			 */
			val letters = arrayOf('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z')

			/**
			 * An array of numbers, used for required/denied character lists
			 * @since 1.0.0
			 */
			val numbers = arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')

			/**
			 * An array of letters and numbers, used for required/denied character lists
			 * @since 1.0.0
			 */
			val lettersNumbers = arrayOf(*letters, *numbers)

			/**
			 * An array of letters numbers and underscores, used for required/denied character lists
			 * @since 1.0.0
			 */
			val lettersNumbersUnderscores = arrayOf(*lettersNumbers, '_')

			/**
			 * An array of letters numbers and spaces, used for required/denied character lists
			 * @since 1.0.0
			 */
			val lettersNumbersSpaces = arrayOf(*lettersNumbers, ' ')

			/**
			 * An array containing the newline, tab, and return control characters, used for required/denied character lists
			 * @since 1.0.0
			 */
			val newlineTabReturn = arrayOf('\n', '\t', '\r')
		}
	}

	private var trim = false
	private var toLowerCase = false
	private var toUpperCase = false
	private var minLen: Int? = null
	private var maxLen: Int? = null
	private var reqChars: Array<Char>? = null
	private var denyChars: Array<Char>? = null
	private var uppercase = false
	private var lowercase = false
	private var regex: Regex? = null
	private var notBlank = false
	private var isInArray: Array<String>? = null
	private var isNotInArray: Array<String>? = null

	override fun validate(param: ParamValidator.Param): ParamValidator.ValidatorResponse {
		var str = param.value

		if(trim)
			str = str.trim()
		if(toLowerCase)
			str = str.lowercase()
		if(toUpperCase)
			str = str.uppercase()

		// Length checks
		if(minLen != null && str.length < minLen!!)
			return ParamValidator.ValidatorResponse(RequestValidationError.DefaultType.INVALID_LENGTH, "The provided string is too short (minimum length is $minLen)")
		if(maxLen != null && str.length > maxLen!!)
			return ParamValidator.ValidatorResponse(RequestValidationError.DefaultType.INVALID_LENGTH, "The provided string is too long (maximum length is $maxLen)")

		// Check if it only has required characters
		if(reqChars != null)
			for(char in str.toCharArray())
				if(!reqChars!!.contains(char))
					return ParamValidator.ValidatorResponse(RequestValidationError.DefaultType.INVALID_CHARS, "The provided string contains invalid characters")

		// Check if it contains denied characters
		if(denyChars != null)
			for(char in str.toCharArray())
				if(denyChars!!.contains(char))
					return ParamValidator.ValidatorResponse(RequestValidationError.DefaultType.INVALID_CHARS, "The provided string contains invalid characters")

		// Check for case
		if(uppercase)
			for(char in str.toCharArray())
				if(char != char.uppercaseChar())
					return ParamValidator.ValidatorResponse(RequestValidationError.DefaultType.INVALID_CASE, "The provided string contains lowercase characters")
		if(lowercase)
			for(char in str.toCharArray())
				if(char != char.lowercaseChar())
					return ParamValidator.ValidatorResponse(RequestValidationError.DefaultType.INVALID_CASE, "The provided string contains uppercase characters")

		// Check if blank
		if(notBlank && str.isBlank())
			return ParamValidator.ValidatorResponse(RequestValidationError.DefaultType.BLANK_STRING, "The provided string is blank")

		// Check for regex
		if(regex != null && !regex!!.matches(str))
			return ParamValidator.ValidatorResponse(RequestValidationError.DefaultType.PATTERN_MISMATCH, "The provided string does not match the required pattern")

		// Check if (not) in arrays
		if(isInArray != null && !isInArray!!.contains(str))
			return ParamValidator.ValidatorResponse(RequestValidationError.DefaultType.INVALID_VALUE, "The provided string is not one of the following: ${isInArray!!.joinToString(", ")}")
		if(isNotInArray != null && isNotInArray!!.contains(str))
			return ParamValidator.ValidatorResponse(RequestValidationError.DefaultType.INVALID_VALUE, "The provided string cannot be any of the following: ${isNotInArray!!.joinToString(", ")}")

		return ParamValidator.ValidatorResponse(str)
	}

	/**
	 * Trims the String before processing or validating it
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun trim(): StringValidator {
		trim = true
		return this
	}

	/**
	 * Makes the String lowercase before processing or validating it
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun toLowerCase(): StringValidator {
		toLowerCase = true
		return this
	}

	/**
	 * Makes the String uppercase before processing or validating it
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun toUpperCase(): StringValidator {
		toUpperCase = true
		return this
	}

	/**
	 * Requires a minimum length
	 * @param length The required minimum length
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun minLength(length: Int): StringValidator {
		minLen = length
		return this
	}
	/**
	 * Requires a maximum length
	 * @param length The required maximum length
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun maxLength(length: Int): StringValidator {
		maxLen = length
		return this
	}

	/**
	 * Requires the String only contain the provided characters
	 * @param chars The required characters
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun requiredCharacters(chars: Array<Char>): StringValidator {
		reqChars = chars
		return this
	}
	/**
	 * Requires the String to not contain the provided characters
	 * @param chars The denied characters
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun denyCharacters(chars: Array<Char>): StringValidator {
		denyChars = chars
		return this
	}

	/**
	 * Requires the String to be all uppercase
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun requireUppercase(): StringValidator {
		uppercase = true
		return this
	}
	/**
	 * Requires the String to be all lowercase
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun requireLowercase(): StringValidator {
		lowercase = true
		return this
	}

	/**
	 * Requires the String to match the provided regex pattern
	 * @param pattern The regex to match
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun regex(pattern: Regex): StringValidator {
		regex = pattern
		return this
	}

	/**
	 * Requires the String to not be blank (contain nothing or only whitespace)
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun notBlank(): StringValidator {
		notBlank = true
		return this
	}

	/**
	 * Requires the String to not contain newlines or control characters (\n, \t, \r, etc).
	 * This must be called AFTER denyCharacters() because it adds the control characters to the list of denied characters.
	 * Alternatively, you can run denyCharacters(StringValidator.CharacterLists.newlineTabReturn), along with any other denied characters.
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun noNewlinesOrControlChars(): StringValidator {
		denyChars = if(denyChars == null)
			CharacterLists.newlineTabReturn
		else
			arrayOf(*denyChars.orEmpty(), *CharacterLists.newlineTabReturn)

		return this
	}

	/**
	 * Requires the String to be in the provided array
	 * @param array The array the String must be in
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun isInArray(array: Array<String>): StringValidator {
		isInArray = array
		return this
	}

	/**
	 * Requires the String to not be in the provided array
	 * @param array The array the String must not be in
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun isNotInArray(array: Array<String>): StringValidator {
		isNotInArray = array
		return this
	}
}