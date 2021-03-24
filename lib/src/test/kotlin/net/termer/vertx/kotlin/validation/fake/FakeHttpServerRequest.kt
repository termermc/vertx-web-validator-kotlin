package net.termer.vertx.kotlin.validation.fake

import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.MultiMap
import io.vertx.core.MultiMap.caseInsensitiveMultiMap
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.*
import io.vertx.core.net.NetSocket
import javax.security.cert.X509Certificate

class FakeHttpServerRequest: HttpServerRequest {
	private val params = caseInsensitiveMultiMap()

	constructor() {
		params["wow"] = "woo"
	}

	override fun exceptionHandler(handler: Handler<Throwable>?): HttpServerRequest {
		TODO("Not yet implemented")
	}

	override fun handler(handler: Handler<Buffer>?): HttpServerRequest {
		TODO("Not yet implemented")
	}

	override fun pause(): HttpServerRequest {
		TODO("Not yet implemented")
	}

	override fun resume(): HttpServerRequest {
		TODO("Not yet implemented")
	}

	override fun fetch(amount: Long): HttpServerRequest {
		TODO("Not yet implemented")
	}

	override fun endHandler(endHandler: Handler<Void>?): HttpServerRequest {
		TODO("Not yet implemented")
	}

	override fun version(): HttpVersion {
		TODO("Not yet implemented")
	}

	override fun method(): HttpMethod {
		TODO("Not yet implemented")
	}

	override fun scheme(): String {
		TODO("Not yet implemented")
	}

	override fun uri(): String {
		TODO("Not yet implemented")
	}

	override fun path(): String {
		TODO("Not yet implemented")
	}

	override fun query(): String {
		TODO("Not yet implemented")
	}

	override fun host(): String {
		TODO("Not yet implemented")
	}

	override fun bytesRead(): Long {
		TODO("Not yet implemented")
	}

	override fun response(): HttpServerResponse {
		TODO("Not yet implemented")
	}

	override fun headers(): MultiMap {
		TODO("Not yet implemented")
	}

	override fun params(): MultiMap = params

	override fun peerCertificateChain(): Array<X509Certificate> {
		TODO("Not yet implemented")
	}

	override fun absoluteURI(): String {
		TODO("Not yet implemented")
	}

	override fun body(): Future<Buffer> {
		TODO("Not yet implemented")
	}

	override fun end(): Future<Void> {
		TODO("Not yet implemented")
	}

	override fun toNetSocket(): Future<NetSocket> {
		TODO("Not yet implemented")
	}

	override fun setExpectMultipart(expect: Boolean): HttpServerRequest {
		TODO("Not yet implemented")
	}

	override fun isExpectMultipart(): Boolean {
		TODO("Not yet implemented")
	}

	override fun uploadHandler(uploadHandler: Handler<HttpServerFileUpload>?): HttpServerRequest {
		TODO("Not yet implemented")
	}

	override fun formAttributes(): MultiMap {
		TODO("Not yet implemented")
	}

	override fun getFormAttribute(attributeName: String?): String {
		TODO("Not yet implemented")
	}

	override fun toWebSocket(): Future<ServerWebSocket> {
		TODO("Not yet implemented")
	}

	override fun isEnded(): Boolean {
		TODO("Not yet implemented")
	}

	override fun customFrameHandler(handler: Handler<HttpFrame>?): HttpServerRequest {
		TODO("Not yet implemented")
	}

	override fun connection(): HttpConnection {
		TODO("Not yet implemented")
	}

	override fun streamPriorityHandler(handler: Handler<StreamPriority>?): HttpServerRequest {
		TODO("Not yet implemented")
	}

	override fun cookieMap(): MutableMap<String, Cookie> {
		TODO("Not yet implemented")
	}
}