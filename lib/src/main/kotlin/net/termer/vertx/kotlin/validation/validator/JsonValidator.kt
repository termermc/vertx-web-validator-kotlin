package net.termer.vertx.kotlin.validation.validator

import io.vertx.core.json.DecodeException
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import net.termer.vertx.kotlin.validation.ParamValidator

/**
 * Validator for JSON object parameters
 * @author termer
 * @since 1.0.0
 */
@Suppress("IMPLICIT_CAST_TO_ANY")
open class JsonValidator: ParamValidator {
	private val reqs = HashMap<String, Class<*>?>()
	private val paramValidators = HashMap<String, ParamValidator>()

	override fun validate(param: ParamValidator.Param): ParamValidator.ValidatorResponse {
		try {
			val json = JsonObject(param.value)
			val map = json.map

			// Validate field types
			for((key, clazz) in reqs.entries) {
				if(!map.containsKey(key)) {
					return ParamValidator.ValidatorResponse("MISSING_FIELD", "The provided JSON object does not contain the field $key")
				} else if(clazz != null && !clazz.isAssignableFrom(map[key]!!::class.java)) {
					return ParamValidator.ValidatorResponse("INVALID_FIELD", "The field $key in the provided JSON object is not the correct type")
				}
			}

			// Validate fields
			for((key, validator) in paramValidators.entries) {
				if(!map.containsKey(key)) {
					return ParamValidator.ValidatorResponse("MISSING_FIELD", "The provided JSON object does not contain the field $key")
				} else {
					// Convert param to string
					@Suppress("UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY")
					val str = when(val toValidate = map[key]) {
						is LinkedHashMap<*, *> -> JsonObject(toValidate as LinkedHashMap<String, *>).toString()
						is ArrayList<*> -> JsonArray(toValidate).toString()
						else -> toValidate.toString()
					}

					// Create param
					val valParam = ParamValidator.Param("${param.field}[\"$key\"]", str, param.context)

					// Validate
					val res = validator.validate(valParam)

					// Send invalid if it failed validation
					if(!res.valid)
						return res
				}
			}

			return ParamValidator.ValidatorResponse(json)
		} catch(e: DecodeException) {
			return ParamValidator.ValidatorResponse("INVALID_JSON", "The provided value does not represent a JSON object")
		}
	}


	/**
	 * Requires the JSON object to contain the specified field
	 * @param field The field's name
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun requireField(field: String): JsonValidator {
		reqs[field] = null
		return this
	}
	/**
	 * Requires the JSON object to contain the specified field as the specified type
	 * @param field The field's name
	 * @param type The type that this field must be
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun requireField(field: String, type: Class<*>): JsonValidator {
		reqs[field] = when(type) {
			JsonObject::class.java -> LinkedHashMap::class.java
			JsonArray::class.java -> ArrayList::class.java
			else -> type
		}

		return this
	}

	/**
	 * Requires that the specified field be validated with the provided validator
	 * @param field The field to validate
	 * @param validator The validator to use
	 * @return This, to be used fluently
	 * @since 1.0.0
	 */
	fun validateFieldWith(field: String, validator: ParamValidator): JsonValidator {
		paramValidators[field] = validator
		return this
	}
}