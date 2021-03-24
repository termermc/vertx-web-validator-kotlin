package net.termer.vertx.kotlin.validation

import io.vertx.core.MultiMap
import io.vertx.ext.web.RoutingContext

/**
 * Simple pluggable request validator
 * @author termer
 * @since 1.0.0
 */
class RequestValidator {
	private val paramValidators = HashMap<String, ValidatorWrapper>()
	private val routeParamValidators = HashMap<String, ValidatorWrapper>()

	private val parsedParams = HashMap<String, Any?>()
	private val parsedRouteParams = HashMap<String, Any?>()

	private var valid = false
	private var errorType: String? = null
	private var errorText: String? = null
	private var errorParam: String? = null

	/**
	 * The validation result's error type, if it failed
	 * @since 1.0.0
	 */
	val validationErrorType: String?
		get() = errorType
	/**
	 * The validation result's plaintext error, if it failed
	 * @since 1.0.0
	 */
	val validationErrorText: String?
		get() = errorText

	/**
	 * The name of the parameter that failed validation, if it failed
	 * @since 1.0.0
	 */
	val validationErrorParam: String?
		get() = errorParam

	/**
	 * Registers a parameter validator for the specified parameter name
	 * @param name The parameter name
	 * @param validator The validator for the parameter
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun param(name: String, validator: ParamValidator): RequestValidator {
		paramValidators[name] = ValidatorWrapper(validator, false, null)
		return this
	}
	/**
	 * Registers an optional parameter validator for the specified parameter name
	 * @param name The parameter name
	 * @param validator The validator for the parameter
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun optionalParam(name: String, validator: ParamValidator): RequestValidator {
		paramValidators[name] = ValidatorWrapper(validator, true, null)
		return this
	}
	/**
	 * Registers an optional parameter validator for the specified parameter name
	 * @param name The parameter name
	 * @param validator The validator for the parameter
	 * @param default The default value to assign if the parameter is not present
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun optionalParam(name: String, validator: ParamValidator, default: Any?): RequestValidator {
		paramValidators[name] = ValidatorWrapper(validator, true, default)
		return this
	}

	/**
	 * Registers a route parameter validator for the specified parameter name
	 * @param name The parameter name
	 * @param validator The validator for the parameter
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun routeParam(name: String, validator: ParamValidator): RequestValidator {
		routeParamValidators[name] = ValidatorWrapper(validator, false, null)
		return this
	}
	/**
	 * Registers an optional route parameter validator for the specified parameter name
	 * @param name The parameter name
	 * @param validator The validator for the parameter
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun optionalRouteParam(name: String, validator: ParamValidator): RequestValidator {
		routeParamValidators[name] = ValidatorWrapper(validator, true, null)
		return this
	}
	/**
	 * Registers an optional route parameter validator for the specified parameter name
	 * @param name The parameter name
	 * @param validator The validator for the parameter
	 * @param default The default value to assign if the route parameter is not present
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun optionalRouteParam(name: String, validator: ParamValidator, default: Any): RequestValidator {
		routeParamValidators[name] = ValidatorWrapper(validator, true, default)
		return this
	}

	fun validate(r: RoutingContext): Boolean {
		val params: MultiMap = r.request().params()
		val routeParams = r.pathParams()

		// Validate params
		for((key, validator) in paramValidators.entries) {
			if(params.contains(key)) {
				val param = ParamValidator.Param(key, params[key]!!, r)

				// Validate parameter
				val res = validator.paramValidator.validate(param)

				// Assign parsed value, otherwise end with error
				if(res.valid) {
					parsedParams[key] = res.result
				} else {
					errorType = res.errorType
					errorText = res.errorText
					errorParam = key
					valid = false
					return false
				}
			} else if(validator.optional) {
				// Assign default value if one is defined
				if(validator.default != null)
					parsedParams[key] = validator.default
			} else {
				// End with missing parameter error
				errorType = "MISSING_PARAM"
				errorText = "Missing parameter \"$key\""
				errorParam = key
				valid = false
				return false
			}
		}

		// Validate route params
		for((key, validator) in routeParamValidators.entries) {
			if(routeParams.contains(key)) {
				val param = ParamValidator.Param(key, routeParams[key]!!, r)

				// Validate parameter
				val res = validator.paramValidator.validate(param)

				// Assign parsed value, otherwise end with error
				if(res.valid) {
					parsedRouteParams[key] = res.result
				} else {
					errorType = res.errorType
					errorText = res.errorText
					errorParam = key
					valid = false
					return false
				}
			} else if(validator.optional) {
				// Assign default value if one is defined
				if(validator.default != null)
					parsedRouteParams[key] = validator.default
			} else {
				// End with missing parameter error
				errorType = "MISSING_ROUTE_PARAM"
				errorText = "Missing route parameter \"$key\""
				errorParam = key
				valid = false
				return false
			}
		}

		return true
	}

	/**
	 * Returns the parsed value of a parameter, or null if it doesn't exist
	 * @param name The parameter's name
	 * @return The parsed value of a parameter
	 * @since 1.0.0
	 */
	fun parsedParam(name: String) = parsedParams[name]
	/**
	 * Returns the parsed value of a route parameter, or null if it doesn't exist
	 * @param name The route parameter's name
	 * @return The parsed value of a route parameter
	 * @since 1.0.0
	 */
	fun parsedRouteParam(name: String) = parsedRouteParams[name]

	/**
	 * Returns whether the specified parameter has been parsed, or has a default value
	 * @param name The name of the parameter to check for
	 * @return Whether the specified parameter has been parsed, or has a default value
	 * @since 1.0.0
	 */
	fun isParamParsed(name: String) = parsedParams.containsKey(name)
	/**
	 * Returns whether the specified route parameter has been parsed, or has a default value
	 * @param name The name of the route parameter to check for
	 * @return Whether the specified route parameter has been parsed, or has a default value
	 * @since 1.0.0
	 */
	fun isRouteParamParsed(name: String) = parsedRouteParams.containsKey(name)

	/**
	 * Class to wrap a validator and options for it
	 * @author termer
	 * @since 1.0.0
	 */
	private class ValidatorWrapper(
			/**
			 * The validator
			 * @since 1.0.0
			 */
			val paramValidator: ParamValidator,
			/**
			 * Whether the parameter this validator is for is optional
			 * @since 1.0.0
			 */
			val optional: Boolean,
			/**
			 * The value to assign if this parameter is not present
			 * @since 1.0.0
			 */
			val default: Any?
	)
}