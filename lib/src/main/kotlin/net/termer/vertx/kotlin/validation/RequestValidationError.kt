package net.termer.vertx.kotlin.validation

/**
 * A request validation error
 * @since 2.0.0
 * @author termer
 */
open class RequestValidationError(
	/**
	 * The error type
	 */
	val type: String,
	val message: String,
	val param: String
) {
	/**
	 * Default validation error types.
	 * Not an exhaustive list; custom validators may return types not included here.
	 * @since 2.0.0
	 */
	class DefaultType {
		companion object {
			/**
			 * A parameter is missing from the request body or query
			 * @since 2.0.0
			 */
			const val MISSING_PARAM = "MISSING_PARAM"

			/**
			 * A route parameter is missing
			 * @since 2.0.0
			 */
			const val MISSING_ROUTE_PARAM = "MISSING_ROUTE_PARAM"

			/**
			 * A parameter's length (either string or array) was too short or too long
			 * @since 2.0.0
			 */
			const val INVALID_LENGTH = "INVALID_LENGTH"

			/**
			 * An invalid boolean value was supplied
			 * @since 2.0.0
			 */
			const val INVALID_BOOL = "INVALID_BOOL"

			/**
			 * A number parameter's value is too large or too small
			 * @since 2.0.0
			 */
			const val INVALID_SIZE = "INVALID_SIZE"

			/**
			 * A byte parameter was malformed
			 * @since 2.0.0
			 */
			const val INVALID_BYTE = "INVALID_BYTE"

			/**
			 * A double parameter was malformed
			 * @since 2.0.0
			 */
			const val INVALID_DOUBLE = "INVALID_DOUBLE"

			/**
			 * A float parameter was malformed
			 * @since 2.0.0
			 */
			const val INVALID_FLOAT = "INVALID_FLOAT"

			/**
			 * An int parameter was malformed
			 * @since 2.0.0
			 */
			const val INVALID_INT = "INVALID_INT"

			/**
			 * A long parameter was malformed
			 * @since 2.0.0
			 */
			const val INVALID_LONG = "INVALID_LONG"

			/**
			 * A parameter contained invalid/forbidden characters
			 * @since 2.0.0
			 */
			const val INVALID_CHARS = "INVALID_CHARS"

			/**
			 * A parameter contains characters of an invalid case (uppercase or lowercase)
			 * @since 2.0.0
			 */
			const val INVALID_CASE = "INVALID_CASE"

			/**
			 * A parameter is a blank string
			 * @since 2.0.0
			 */
			const val BLANK_STRING = "BLANK_STRING"

			/**
			 * A parameter did not match a required pattern
			 * @since 2.0.0
			 */
			const val PATTERN_MISMATCH = "PATTERN_MISMATCH"

			/**
			 * A parameter did not match any required values
			 * @since 2.0.0
			 */
			const val INVALID_VALUE = "INVALID_VALUE"

			/**
			 * A parameter is not a valid email address
			 * @since 2.0.0
			 */
			const val INVALID_EMAIL = "INVALID_EMAIL"

			/**
			 * An array parameter contained an invalid item
			 * @since 2.0.0
			 */
			const val INVALID_ITEM = "INVALID_ITEM"

			/**
			 * A parameter is not valid JSON
			 * @since 2.0.0
			 */
			const val INVALID_JSON = "INVALID_JSON"

			/**
			 * A JSON object parameter is missing a required field
			 * @since 2.0.0
			 */
			const val MISSING_FIELD = "MISSING_FIELD"

			/**
			 * A JSON object parameter has an invalid field
			 * @since 2.0.0
			 */
			const val INVALID_FIELD = "INVALID_FIELD"

			/**
			 * A date parameter is outside the required time range
			 * @since 2.0.0
			 */
			const val INVALID_TIME = "INVALID_TIME"

			/**
			 * A parameter is not a valid date
			 * @since 2.0.0
			 */
			const val INVALID_DATE = "INVALID_DATE"
		}
	}
}