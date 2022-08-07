package net.termer.vertx.kotlin.validation

import net.termer.vertx.kotlin.validation.exception.RequestValidationException
import net.termer.vertx.kotlin.validation.fake.FakeRoutingContext
import net.termer.vertx.kotlin.validation.validator.AnyValidator
import net.termer.vertx.kotlin.validation.validator.IntValidator
import net.termer.vertx.kotlin.validation.validator.NoneValidator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Tests specifically relating to [RequestValidator]
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

		assert(valid)

		// Check for parsed values
		val intParam = v.parsedParam("intParam")
		assertEquals(intParam, 1)
		val intRouteParam = v.parsedRouteParam("intRouteParam")
		assertEquals(intRouteParam, 2)
	}

	@Test
	fun `Report single missing param`() {
		val r = FakeRoutingContext()

		val v = RequestValidator()
				.param("paramName", AnyValidator())

		val valid = v.validate(r)

		// Returns invalid
		assert(!valid)

		// Only has one validation error
		assertEquals(v.validationErrors.size, 1)
		val error = v.validationErrors[0]

		// Verify error is correct
		assertEquals(error.type, RequestValidationError.DefaultType.MISSING_PARAM)
		assertEquals(error.param, "paramName")
		assert(error.message.contains("paramName"))
	}

	@Test
	fun `Report multiple missing params`() {
		val r = FakeRoutingContext()
		r.request().params()["param1"] = "test data"
		r.request().params()["param2"] = "stuff"
		r.request().params()["param3"] = "things"

		val v = RequestValidator()
			.param("param1", NoneValidator())
			.param("param2", NoneValidator())
			.param("param3", AnyValidator())

		val valid = v.validate(r)

		assert(!valid)
		assertEquals(v.validationErrors.size, 2)
	}

	@Test
	fun `Parse valid params even if there are bad params`() {
		val r = FakeRoutingContext()
		r.request().params()["bad"] = "test data"
		r.request().params()["good"] = "100"

		val v = RequestValidator()
			.param("bad", NoneValidator())
			.param("good", IntValidator())

		val valid = v.validate(r)

		assert(!valid)
		assertEquals(v.validationErrors.size, 1)

		assert(v.isParamParsed("good"))
		assertEquals(v.parsedParam("good"), 100)
	}

	@Test
	fun `validateOrThrowException throws an exception on bad params`() {
		val r = FakeRoutingContext()
		r.request().params()["bad"] = "test data"

		val v = RequestValidator()
			.param("bad", NoneValidator())

		assertThrows<RequestValidationException> {
			v.validateOrThrowException(r)
		}
	}
}