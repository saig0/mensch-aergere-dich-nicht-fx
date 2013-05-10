package rest

import dispatch._
import Defaults._
import net.liftweb.json._
import net.liftweb.json.Serialization._
import model.Game
import com.ning.http.client.RequestBuilder

object GameServerClient {

	implicit val formats = DefaultFormats

	private def gameServer = host("rest-game-server.herokuapp.com")

	private val jsonHeader = Map("Content-Type" -> "application/json")

	def getAvaiableGames: List[Game] = {
		val request = gameServer / "games" GET

		handleRequest(request, { result =>
			val json = parse(result)
			json.extract[List[Game]]
		})
	}

	def newGame(maxPlayers: Int, currentPlayers: Int): Game = {
		val game = Game(0, "ip", maxPlayers, currentPlayers)
		val json = write(game)

		val request = jsonRequest(gameServer / "games" POST, json)
		handleRequest(request, { result =>
			val newGameAsJson = parse(result)
			newGameAsJson.extract[Game]
		})
	}

	def updateGame(game: Game) {
		val json = write(game)

		val request = jsonRequest(gameServer / "games" / game.id PUT, json)
		handleRequest(request, result => ())
	}

	def deleteGame(game: Game) {
		val request = gameServer / "games" / game.id DELETE

		handleRequest(request, result => ())
	}

	private def jsonRequest(request: RequestBuilder, json: String): RequestBuilder =
		request << json <:< jsonHeader

	private def handleRequest[T](request: RequestBuilder, handler: String => T): T = {
		Http(request OK as.String).either() match {
			case Right(content) => handler(content)
			case Left(StatusCode(404)) => throw new RuntimeException("request failed with status: not found")
			case Left(StatusCode(code)) => throw new RuntimeException("request failed with status: " + code)
			case Left(e) => throw new RuntimeException("request failed: " + e)
		}
	}
}