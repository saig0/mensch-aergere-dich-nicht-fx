package ui.presenter

import model.Player
import ui.view.StartView
import ui.StartEvent
import ui.GoToStart
import ui.GoToGameCreation
import ui.GoToJoinGame
import ui.GoToConnectIp

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

	def connectIp {
		publish(GoToConnectIp(player))
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