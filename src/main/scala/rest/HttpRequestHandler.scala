package rest

import dispatch._
import Defaults._
import com.ning.http.client.RequestBuilder

trait HttpRequestHandler {

	val jsonHeader = Map("Content-Type" -> "application/json")

	def jsonRequest(request: RequestBuilder, json: String): RequestBuilder =
		request << json <:< jsonHeader

	def handleRequest[T](request: RequestBuilder, handler: String => T): T = {
		Http(request OK as.String).either() match {
			case Right(content) => handler(content)
			case Left(StatusCode(404)) => throw new RuntimeException("request failed with status: not found")
			case Left(StatusCode(code)) => throw new RuntimeException("request failed with status: " + code)
			case Left(e) => throw new RuntimeException("request failed: " + e)
		}
	}
}

