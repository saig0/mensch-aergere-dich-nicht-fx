package ui.presenter

import scala.concurrent._
import ExecutionContext.Implicits.global
import model.Game
import rest.GameServerClient

trait GameCreationBehavior {
	def createGame: Future[Game]

	def updateGame(updatedGame: Game): Future[Unit]

	def deleteGame(game: Game): Future[Unit]
}

object GameCreationBehavior {
	def apply(local: Boolean) =
		if (local) {
			new LocalGameCreationBehavior
		} else {
			new HostedGameCreationBehavior
		}
}

class HostedGameCreationBehavior extends GameCreationBehavior {
	def createGame = GameServerClient.newGame(4, 0)

	def updateGame(updatedGame: Game) = GameServerClient.updateGame(updatedGame)

	def deleteGame(game: Game) = GameServerClient.deleteGame(game)
}

class LocalGameCreationBehavior extends GameCreationBehavior {
	def createGame = future(Game(0, "127.0.0.1", 4, 0))

	def updateGame(updatedGame: Game) = future(Unit)

	def deleteGame(game: Game) = future(Unit)
}