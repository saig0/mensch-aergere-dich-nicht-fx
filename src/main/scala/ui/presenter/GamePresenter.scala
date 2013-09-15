package ui.presenter

import ui.view.GameView
import ui.GoToGame

class GamePresenter extends Presenter[GameView] {

	lazy val view = new GameView(this)

	val events = List(GoToGame(Nil))

	def receive = {
		case GoToGame(players) => {
			createView
			updateUi {
				view.start(players)
			}
		}
	}
}