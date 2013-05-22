package ui

import model.Player
import rest.GameServerClient
import model.Game
import communication.Client
import akka.actor.ActorRef

class JoinGamePresenter extends Presenter[JoinGameView] {

	lazy val view = new JoinGameView(this)

	val events = List(GoToJoinGame(Player("")))

	var clientServer: ActorRef = _

	def receive = {
		case GoToJoinGame(player) => {
			createView
			updateUi(GameServerClient.getAvaiableGames, { games: List[Game] =>
				view.showGames(games)
			})
		}
	}

	def joinGame {
		val game = view.selectedGame
		println("join " + game)
		clientServer = Client.create(game.address)
		clientServer ! "Client is up!"
	}

	def loadGames {
		view.showLoading
		updateUi(GameServerClient.getAvaiableGames, { games: List[Game] =>
			view.showGames(games)
		})
	}

	def abort {
		publish(GoToStart)
	}
}