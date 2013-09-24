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

	val events = List(NewTurn(Player(""), 0))

	val startEvent = GoToGame(Nil, Player(""))

	var selfPlayer: Player = _

	def onStart = {
		case GoToGame(players, self) => {
			this.selfPlayer = self
			updateUi {
				view.players(players)
			}
		}
	}

	def on = {
		case NewTurn(player, number) if (player == selfPlayer) => {
			updateUi {
				view.yourTurn(player)
				view.dice(number)
			}
		}
		case NewTurn(player, number) => {
			updateUi {
				view.newTurn(player)
				view.dice(number)
			}
		}
	}

	def onEnd {}
}