package net.termer.vertx.kotlin.validation.fake

import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.MultiMap
import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.Cookie
import io.vertx.core.http.HttpMethod
import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.User
import io.vertx.ext.web.*
import java.nio.charset.Charset

class FakeRoutingContext: RoutingContext {
	private val pathParams = HashMap<String, String>()
	private val req = FakeHttpServerRequest()

	var nextCalled = false
	var failCalled = false
	var failStatus: Int? = null
	var failThrowable: Throwable? = null

	override fun request() = req
	override fun response(): HttpServerResponse {
		TODO("Not yet implemented")
	}

	override fun next() {
		nextCalled = true
	}

	override fun fail(statusCode: Int) {
		failCalled = true
		failStatus = statusCode
	}

	override fun fail(throwable: Throwable?) {
		failCalled = true
		failThrowable = throwable
	}

	override fun fail(statusCode: Int, throwable: Throwable?) {
		failCalled = true
		failStatus = statusCode
		failThrowable = throwable
	}

	override fun put(key: String?, obj: Any?): RoutingContext {
		TODO("Not yet implemented")
	}

	override fun <T : Any?> get(key: String?): T {
		TODO("Not yet implemented")
	}

	override fun <T : Any?> get(key: String?, defaultValue: T): T {
		TODO("Not yet implemented")
	}

	override fun <T : Any?> remove(key: String?): T {
		TODO("Not yet implemented")
	}

	override fun data(): MutableMap<String, Any> {
		TODO("Not yet implemented")
	}

	override fun vertx(): Vertx {
		TODO("Not yet implemented")
	}

	override fun mountPoint(): String {
		TODO("Not yet implemented")
	}

	override fun currentRoute(): Route {
		TODO("Not yet implemented")
	}

	override fun normalizedPath(): String {
		TODO("Not yet implemented")
	}

	override fun getCookie(name: String?): Cookie {
		TODO("Not yet implemented")
	}

	override fun addCookie(cookie: Cookie?): RoutingContext {
		TODO("Not yet implemented")
	}

	override fun removeCookie(name: String?, invalidate: Boolean): Cookie {
		TODO("Not yet implemented")
	}

	override fun cookieCount(): Int {
		TODO("Not yet implemented")
	}

	override fun cookieMap(): MutableMap<String, Cookie> {
		TODO("Not yet implemented")
	}

	override fun getBodyAsString(): String {
		TODO("Not yet implemented")
	}

	override fun getBodyAsString(encoding: String?): String {
		TODO("Not yet implemented")
	}

	override fun getBodyAsJson(maxAllowedLength: Int): JsonObject {
		TODO("Not yet implemented")
	}

	override fun getBodyAsJsonArray(maxAllowedLength: Int): JsonArray {
		TODO("Not yet implemented")
	}

	override fun getBody(): Buffer {
		TODO("Not yet implemented")
	}

	override fun body(): RequestBody {
		TODO("Not yet implemented")
	}

	override fun fileUploads(): MutableList<FileUpload> {
		TODO("Not yet implemented")
	}

	override fun session(): Session {
		TODO("Not yet implemented")
	}

	override fun isSessionAccessed(): Boolean {
		TODO("Not yet implemented")
	}

	override fun user(): User {
		TODO("Not yet implemented")
	}

	override fun failure(): Throwable {
		TODO("Not yet implemented")
	}

	override fun statusCode(): Int {
		TODO("Not yet implemented")
	}

	override fun getAcceptableContentType(): String {
		TODO("Not yet implemented")
	}

	override fun parsedHeaders(): ParsedHeaderValues {
		TODO("Not yet implemented")
	}

	override fun addHeadersEndHandler(handler: Handler<Void>?): Int {
		TODO("Not yet implemented")
	}

	override fun removeHeadersEndHandler(handlerID: Int): Boolean {
		TODO("Not yet implemented")
	}

	override fun addBodyEndHandler(handler: Handler<Void>?): Int {
		TODO("Not yet implemented")
	}

	override fun removeBodyEndHandler(handlerID: Int): Boolean {
		TODO("Not yet implemented")
	}

	override fun addEndHandler(handler: Handler<AsyncResult<Void>>?): Int {
		TODO("Not yet implemented")
	}

	override fun removeEndHandler(handlerID: Int): Boolean {
		TODO("Not yet implemented")
	}

	override fun failed(): Boolean {
		TODO("Not yet implemented")
	}

	override fun setBody(body: Buffer?) {
		TODO("Not yet implemented")
	}

	override fun setSession(session: Session?) {
		TODO("Not yet implemented")
	}

	override fun setUser(user: User?) {
		TODO("Not yet implemented")
	}

	override fun clearUser() {
		TODO("Not yet implemented")
	}

	override fun setAcceptableContentType(contentType: String?) {
		TODO("Not yet implemented")
	}

	override fun reroute(method: HttpMethod?, path: String?) {
		TODO("Not yet implemented")
	}

	override fun pathParams() = pathParams
	override fun pathParam(name: String?) = pathParams[name]

	override fun queryParams(): MultiMap {
		TODO("Not yet implemented")
	}

	override fun queryParams(encoding: Charset?): MultiMap {
		TODO("Not yet implemented")
	}

	override fun queryParam(name: String?): MutableList<String> {
		TODO("Not yet implemented")
	}
}