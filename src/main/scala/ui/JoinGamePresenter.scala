package ui

import model.Player
import rest.GameServerClient
import model.Game

class JoinGamePresenter extends Presenter[JoinGameView] {

	lazy val view = new JoinGameView(this)

	val events = List(GoToJoinGame(Player("")))

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