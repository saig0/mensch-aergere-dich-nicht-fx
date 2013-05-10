package test

import dispatch._
import Defaults._

object Main {

	def main(args: Array[String]) {
		println("Hallo")

		val request = host("rest-game-server.herokuapp.com") / "games" GET
		val result = Http(request OK as.String)
		val games = result()
		println(games)

		Thread.sleep(5000)
	}
}