package ui

import rest.GameServerClient
import model.Game
import model.Player
import model.Player

class GameCreationPresenter extends Presenter[GameCreationView] {

	lazy val view = new GameCreationView(this)

	val events = List(GoToGameCreation(Player("")))

	def receive = {
		case GoToGameCreation(player) => {
			createView
			updateUi(GameServerClient.newGame(4, 1), { (game: Game) =>
				view.showGame(game)
				view.joinPlayer(player)
			})
		}
		case JoinPlayer(player) => {
			view.joinPlayer(player)
		}
	}
}