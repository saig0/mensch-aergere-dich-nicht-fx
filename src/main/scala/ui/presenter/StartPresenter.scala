package ui.presenter

import model.Player
import ui.view.StartView
import ui.GoToStart
import ui.GoToGameCreation
import ui.GoToJoinGame
import ui.GoToConnectIp
import ui.ClientEvent
import ui.GoToGameCreation

class StartPresenter extends Presenter[StartView] {

	lazy val view = new StartView(StartPresenter.this)

	val events = List[ClientEvent]()

	val startEvent = GoToStart()

	def onStart = {
		case _ =>
	}

	def onEnd {}

	def player = Player(view.playerName)

	def createHostedGame {
		publish(GoToGameCreation(player, false))
	}

	def createLocalGame {
		publish(GoToGameCreation(player, true))
	}

	def joinGame {
		publish(GoToJoinGame(player))
	}

	def connectIp {
		publish(GoToConnectIp(player))
	}

	def on = {
		case _ =>
	}
}