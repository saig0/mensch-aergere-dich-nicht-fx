package rest

import dispatch._
import dispatch.Defaults._
import net.liftweb.json._
import net.liftweb.json.Serialization._
import rest.GameServerClient

object RestDemo {

	implicit val formats = DefaultFormats

	def main(args: Array[String]) {
		println("get")
		GameServerClient.getAvaiableGames() map println

		println("create")
		val game = GameServerClient.newGame(4, 1)()
		println(game)

		println("update")
		GameServerClient.updateGame(game.copy(currentPlayers = 2))
		GameServerClient.getAvaiableGames map println

		println("delete")
		GameServerClient.deleteGame(game);
		GameServerClient.getAvaiableGames map println
	}

	def removeGames {
		GameServerClient.getAvaiableGames() map { game =>
			println("remove " + game)
			GameServerClient.deleteGame(game)
			Thread.sleep(1000)
		}
	}
}