package rest

import dispatch._
import Defaults._
import net.liftweb.json._
import net.liftweb.json.Serialization._
import com.ning.http.client.RequestBuilder
import model.Game
import scala.concurrent.future

object GameServerClient extends HttpRequestHandler {

	private implicit val formats = DefaultFormats

	private def gameServer = host("rest-game-server.herokuapp.com")

	def getAvaiableGames: Future[List[Game]] = {
		val request = gameServer / "games" GET

		handleRequest(request, { result =>
			val json = parse(result)
			json.extract[List[Game]]
		})
	}

	def newGame(maxPlayers: Int, currentPlayers: Int): Future[Game] = {
		val game = Game(0, "ip", maxPlayers, currentPlayers)
		val json = write(game)

		val request = jsonRequest(gameServer / "games" POST, json)
		handleRequest(request, { result =>
			val newGameAsJson = parse(result)
			newGameAsJson.extract[Game]
		})
	}

	def updateGame(game: Game): Future[Unit] = future {
		val json = write(game)

		val request = jsonRequest(gameServer / "games" / game.id PUT, json)
		handleRequest(request, result => ())
	}

	def deleteGame(game: Game): Future[Unit] = future {
		val request = gameServer / "games" / game.id DELETE

		handleRequest(request, result => ())
	}

}