package test

import dispatch._
import Defaults._
import net.liftweb.json._
import net.liftweb.json.Serialization._
import model.Game

object Main {

	implicit val formats = DefaultFormats

	def main(args: Array[String]) {
		println("Hallo")

		val request = host("rest-game-server.herokuapp.com") / "games" GET
		val result = Http(request OK as.String)

		val json = parse(result())
		val games = json.extract[List[Game]]

		games map println

		println(write(games))
	}
}