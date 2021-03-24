package net.termer.vertx.kotlin.validation.validator

import io.vertx.core.json.DecodeException
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import net.termer.vertx.kotlin.validation.ParamValidator

/**
 * Validator for JSON array parameters
 * @author termer
 * @since 1.0.0
 */
open class JsonArrayValidator: ParamValidator {
	private var noNulls = false
	private var allowedTypes: Array<Class<*>>? = null
	private var minLen: Int? = null
	private var maxLen: Int? = null
	private var arrayValidator: JsonArrayValidator? = null
	private var objectValidator: JsonValidator? = null

	override fun validate(param: ParamValidator.Param): ParamValidator.ValidatorResponse {
		try {
			val arr = JsonArray(param.value)
			val list = arr.list

			// Check length
			if(minLen != null && list.size < minLen!!)
				return ParamValidator.ValidatorResponse("INVALID_LENGTH", "The JSON array is too short (minimum length is $minLen)")
			if(maxLen != null && list.size > maxLen!!)
				return ParamValidator.ValidatorResponse("INVALID_LENGTH", "The JSON array is too long (maximum length is $maxLen)")

			// Check for nulls
			if(noNulls || allowedTypes != null) {
				for((i, item) in list.withIndex()) {
					if(item == null) {
						return ParamValidator.ValidatorResponse("INVALID_ITEM", "The JSON array cannot contain any nulls, and the item at index $i is null")
					}
				}
			}

			// Validate types
			if(allowedTypes != null) {
				for((i, item) in list.withIndex()) {
					var goodType = false
					for(type in allowedTypes!!) {
						if(type.isAssignableFrom(item!!::class.java)) {
							goodType = true
							break
						}
					}

					if(!goodType)
						return ParamValidator.ValidatorResponse("INVALID_ITEM", "The item at index $i of the JSON array is not the correct type")
				}
			}

			// Validate sub-arrays
			if(arrayValidator != null || objectValidator != null) {
				for((i, item) in list.withIndex()) {
					// Check if this is an array or object, and see if they need to be validated
					if(arrayValidator != null && item is ArrayList<*>) {
						val arrParam = ParamValidator.Param("${param.field}[$i]", JsonArray(item).toString(), param.context)
						val res = arrayValidator!!.validate(arrParam)

						// Send invalid if it failed validation
						if(!res.valid)
							return res
					} else if(objectValidator != null && item is LinkedHashMap<*, *>) {
						@Suppress("UNCHECKED_CAST")
						val arrParam = ParamValidator.Param("${param.field}[$i]", JsonObject(item as LinkedHashMap<String, *>).toString(), param.context)
						val res = objectValidator!!.validate(arrParam)

						// Send invalid if it failed validation
						if(!res.valid)
							return res
					}
				}
			}

			return ParamValidator.ValidatorResponse(arr)
		} catch(e: DecodeException) {
			return ParamValidator.ValidatorResponse("INVALID_JSON", "The provided value does not represent a JSON array")
		}
	}

	/**
	 * Requires all array items to not be null
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun requireNoNulls(): JsonArrayValidator {
		noNulls = true
		return this
	}

	/**
	 * Only allows the specified types for array items
	 * @param types An array of allowed array item types
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun onlyAllowTypes(types: Array<Class<*>>): JsonArrayValidator {
		allowedTypes = types
		return this
	}

	/**
	 * Requires a minimum length
	 * @param length The required minimum length
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun minLength(length: Int): JsonArrayValidator {
		minLen = length
		return this
	}
	/**
	 * Requires a maximum length
	 * @param length The required maximum length
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun maxLength(length: Int): JsonArrayValidator {
		maxLen = length
		return this
	}

	/**
	 * Validates sub-arrays (arrays in this array) with the provided JsonArrayValidator
	 * @param validator The validator to validate sub-arrays with
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun validateSubArraysWith(validator: JsonArrayValidator): JsonArrayValidator {
		arrayValidator = validator
		return this
	}

	/**
	 * Validates sub-objects (objects in this array) with the provided JsonValidator
	 * @param validator The validator to validate sub-objects with
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun validateSubObjectsWith(validator: JsonValidator): JsonArrayValidator {
		objectValidator = validator
		return this
	}
}