package ui.presenter

import ui.view.GameView
import ui.GoToGame
import ui.NavigationEvent
import ui.ClientEvent

class GamePresenter extends Presenter[GameView] {

	lazy val view = new GameView(this)

	val events = List[ClientEvent]()

	val startEvent = GoToGame(Nil)

	def onStart = {
		case GoToGame(players) => {
			updateUi {
				view.start(players)
			}
		}
	}

	def on = {
		case _ =>
	}

	def onEnd {}
}