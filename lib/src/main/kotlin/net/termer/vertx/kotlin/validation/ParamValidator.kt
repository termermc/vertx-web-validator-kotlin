package net.termer.vertx.kotlin.validation

import io.vertx.ext.web.RoutingContext

/**
 * Interface to be implemented by parameter validators
 * @author termer
 * @since 1.0.0
 */
interface ParamValidator {
	/**
	 * Returns whether the provided parameter is valid
	 * @param param The parameter to validate
	 * @return Whether the provided parameter is valid
	 * @since 1.0.0
	 */
	fun validate(param: Param): ValidatorResponse

	/**
	 * Data object for containing info about a field for validation
	 * @author termer
	 * @since 1.0.0
	 */
	class Param(
			/**
			 * The field name
			 * @since 1.0.0
			 */
			val field: String,
			/**
			 * The field's string value
			 * @since 1.0.0
			 */
			val value: String,
			/**
			 * The RoutingContext associated with the parameter
			 * @since 1.0.0
			 */
			val context: RoutingContext
	)

	/**
	 * Data object for containing info on a validator's response
	 * @author termer
	 * @since 1.0.0
	 */
	class ValidatorResponse {
		/**
		 * Whether the parameter was valid
		 * @since 1.0.0
		 */
		val valid: Boolean

		/**
		 * The parsed result of this parameter.
		 * This will only be null if the response is an error.
		 * @since 1.0.0
		 */
		val result: Any?

		/**
		 * The type of error (computer-friendly name) of the error in this parameter
		 * @since 1.0.0
		 */
		val errorType: String?

		/**
		 * The plain text error in this parameter
		 * @since 1.0.0
		 */
		val errorText: String?

		/**
		 * Creates a succeeded response
		 * @param res The value of the parameter once parsed
		 * @since 1.0.0
		 */
		constructor(res: Any) {
			this.result = res
			this.errorType = null
			this.errorText = null
			this.valid = true
		}
		/**
		 * Creates an error response
		 * @param errorType The type of error (computer-friendly name) of the error in this parameter
		 * @param errorText The plain text error in this parameter
		 * @since 1.0.0
		 */
		constructor(errorType: String, errorText: String) {
			this.result = null
			this.errorType = errorType
			this.errorText = errorText
			this.valid = false
		}
	}
}