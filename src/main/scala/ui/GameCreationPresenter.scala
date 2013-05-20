package ui

import rest.GameServerClient
import model.Game
import model.Player
import model.Player

class GameCreationPresenter extends Presenter[GameCreationView] {

	lazy val view = new GameCreationView(this)

	val events = List(GoToGameCreation(Player("")), EndEvent)

	var game: Game = _

	def receive = {
		case GoToGameCreation(player) => {
			createView
			updateUi(GameServerClient.newGame(4, 1), { (game: Game) =>
				this.game = game
				view.showGame(game)
				view.joinPlayer(player)
			})
		}
		case JoinPlayer(player) => {
			view.joinPlayer(player)
		}
		case EndEvent => {
			GameServerClient.deleteGame(this.game)
		}
	}

	def abort {
		GameServerClient.deleteGame(this.game)
		publish(GoToStart)
	}
}