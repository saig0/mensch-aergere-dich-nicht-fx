package ui

import model.Player

class StartPresenter extends Presenter[StartView] {

	lazy val view = new StartView(StartPresenter.this)

	val events = List(StartEvent)

	def createGame {
		val player = Player(view.playerName)
		publish(GoToGameCreation(player))
	}

	def joinGame {}

	def receive = {
		case StartEvent => {
			createView
		}
	}
}