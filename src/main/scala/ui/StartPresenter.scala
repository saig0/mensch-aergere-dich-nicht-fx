package ui

import model.Player

class StartPresenter extends Presenter[StartView] {

	lazy val view = new StartView(StartPresenter.this)

	val events = List(StartEvent, GoToStart)

	def player = Player(view.playerName)

	def createGame {
		publish(GoToGameCreation(player))
	}

	def joinGame {
		publish(GoToJoinGame(player))
	}

	def receive = {
		case StartEvent => {
			createView
		}
		case GoToStart => {
			createView
		}
	}
}