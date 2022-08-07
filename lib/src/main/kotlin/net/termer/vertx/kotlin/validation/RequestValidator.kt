package net.termer.vertx.kotlin.validation

import io.vertx.core.MultiMap
import io.vertx.ext.web.RoutingContext
import net.termer.vertx.kotlin.validation.exception.RequestValidationException

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
	private var errors: Array<RequestValidationError> = emptyArray()

	/**
	 * Returns whether the request was found to be valid after calling [validate].
	 * Will return false if [validate] has not been called yet.
	 * @since 2.0.0
	 */
	val isValid: Boolean
		get() = valid
	/**
	 * All validation errors found after calling [validate]
	 * @since 2.0.0
	 */
	val validationErrors: Array<RequestValidationError>
		get() = errors

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

	/**
	 * Validates a request using this validator and all its options.
	 * Returns true if the validation was successful, false if there were any parameters that did not pass validation.
	 * @param ctx The routing context of the request to validate
	 * @return Whether the validation was successful
	 * @since 2.0.0
	 */
	fun validate(ctx: RoutingContext): Boolean {
		val params: MultiMap = ctx.request().params()
		val routeParams = ctx.pathParams()

		// Errors found while validating params
		val errorsList = arrayListOf<RequestValidationError>()

		// Validate params
		for((key, validator) in paramValidators.entries) {
			if(params.contains(key)) {
				val param = ParamValidator.Param(key, params[key]!!, ctx)

				// Validate parameter
				val res = validator.paramValidator.validate(param)

				// Assign parsed value, otherwise add error to list
				if(res.valid)
					parsedParams[key] = res.result
				else
					errorsList.add(RequestValidationError(res.errorType!!, res.errorMessage!!, key))
			} else if(validator.optional) {
				// Assign default value if one is defined
				if(validator.default != null)
					parsedParams[key] = validator.default
			} else {
				// Add missing parameter error to list
				errorsList.add(RequestValidationError(RequestValidationError.DefaultType.MISSING_PARAM, "Missing parameter \"$key\"", key))
			}
		}

		// Validate route params
		for((key, validator) in routeParamValidators.entries) {
			if(routeParams.contains(key)) {
				val param = ParamValidator.Param(key, routeParams[key]!!, ctx)

				// Validate parameter
				val res = validator.paramValidator.validate(param)

				// Assign parsed value, otherwise add error to list
				if(res.valid)
					parsedRouteParams[key] = res.result
				else
					errorsList.add(RequestValidationError(res.errorType!!, res.errorMessage!!, key))
			} else if(validator.optional) {
				// Assign default value if one is defined
				if(validator.default != null)
					parsedRouteParams[key] = validator.default
			} else {
				// Add missing parameter error to list
				errorsList.add(RequestValidationError(RequestValidationError.DefaultType.MISSING_ROUTE_PARAM, "Missing route parameter \"$key\"", key))
			}
		}

		// Determine validity and populate errors array
		valid = errorsList.isEmpty()
		errors = errorsList.toTypedArray()

		return valid
	}

	/**
	 * Validates a request using this validator and all its options.
	 * Throws a [RequestValidationException] if the validation failed.
	 * @param ctx The routing context of the request to validate
	 * @throws RequestValidationException If the validation failed
	 * @since 2.0.0
	 */
	fun validateOrThrowException(ctx: RoutingContext) {
		if(!validate(ctx))
			throw RequestValidationException("Validation failed (${errors.size} error(s) found during validation)", this, ctx)
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