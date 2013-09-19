package ui.presenter

import ui.view.GameView
import ui.GoToGame
import ui.NavigationEvent
import ui.ClientEvent
import ui.NewTurn
import model.Player
import ui.NewTurn

class GamePresenter extends Presenter[GameView] {

	lazy val view = new GameView(this)

	val events = List(NewTurn(Player("")))

	val startEvent = GoToGame(Nil, Player(""))

	var selfPlayer: Player = _

	def onStart = {
		case GoToGame(players, self) => {
			this.selfPlayer = self
			updateUi {
				view.showLoading("Spiel beginnt! Spieler: " + players.map(_.name).mkString(", "))
			}
		}
	}

	def on = {
		case NewTurn(player) if (player == selfPlayer) => {
			updateUi {
				view.yourTurn(player)
			}
		}
		case NewTurn(player) => {
			updateUi {
				view.newTurn(player)
			}
		}
	}

	def onEnd {}
}