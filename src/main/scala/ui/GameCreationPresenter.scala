package ui

import rest.GameServerClient
import model.Game
import model.Player

class GameCreationPresenter extends Presenter[GameCreationView] {

	val view = new GameCreationView(this)

	def show(player: Player) {
		updateUi(GameServerClient.newGame(4, 1), { (game: Game) =>
			view.showGame(game)
			view.joinPlayer(player)
		})
	}
}