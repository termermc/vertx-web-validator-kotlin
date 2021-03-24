package net.termer.vertx.kotlin.validation

import net.termer.vertx.kotlin.validation.fake.FakeRoutingContext
import net.termer.vertx.kotlin.validation.validator.AnyValidator
import net.termer.vertx.kotlin.validation.validator.IntValidator
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Tests tasks specifically relating to RequestValidator
 * @author termer
 */
class RequestValidatorTest {
	@Test
	fun `Handle params and route params`() {
		val r = FakeRoutingContext()
		r.request().params()["param"] = "value"
		r.pathParams()["routeParam"] = "value"

		// Check for pre-defined params and route params
		val v = RequestValidator()
				.param("param", AnyValidator())
				.routeParam("routeParam", AnyValidator())

		val valid = v.validate(r)

		if(!valid)
			println("Param ${v.validationErrorParam} cannot be validated: ${v.validationErrorText} (${v.validationErrorType})")

		assert(valid)
	}

	@Test
	fun `Handle optional params and route params`() {
		val r = FakeRoutingContext()

		// Optionally require params and route params, and give some of them default values
		val v = RequestValidator()
				.optionalParam("param", AnyValidator())
				.optionalParam("paramWithValue", AnyValidator(), "value")
				.optionalRouteParam("routeParam", AnyValidator())
				.optionalRouteParam("routeParamWithValue", AnyValidator(), "routeValue")

		val valid = v.validate(r)

		if(!valid)
			println("Param ${v.validationErrorParam} cannot be validated: ${v.validationErrorText} (${v.validationErrorType})")

		assert(valid)

		// Check for null and default values
		val param = v.parsedParam("param")
		assertNull(param)
		val paramWithValue = v.parsedParam("paramWithValue")
		assertEquals(paramWithValue, "value")

		val routeParam = v.parsedRouteParam("routeParam")
		assertNull(routeParam)
		val routeParamWithValue = v.parsedRouteParam("routeParamWithValue")
		assertEquals(routeParamWithValue, "routeValue")
	}

	@Test
	fun `Parse strings to ints`() {
		val r = FakeRoutingContext()
		r.request().params()["intParam"] = "1"
		r.pathParams()["intRouteParam"] = "2"

		val v = RequestValidator()
				.param("intParam", IntValidator())
				.routeParam("intRouteParam", IntValidator())

		val valid = v.validate(r)

		if(!valid)
			println("Param ${v.validationErrorParam} cannot be validated: ${v.validationErrorText} (${v.validationErrorType})")

		assert(valid)

		// Check for parsed values
		val intParam = v.parsedParam("intParam")
		assertEquals(intParam, 1)
		val intRouteParam = v.parsedRouteParam("intRouteParam")
		assertEquals(intRouteParam, 2)
	}

	@Test
	fun `Report missing params`() {
		val r = FakeRoutingContext()

		val v = RequestValidator()
				.param("paramName", AnyValidator())

		val valid = v.validate(r)

		assert(!valid)

		assertEquals(v.validationErrorType, "MISSING_PARAM")
		assertEquals(v.validationErrorParam, "paramName")
		assert(v.validationErrorText!!.contains("paramName"))
	}
}