package ui.presenter

import ui.view.GameView
import ui.GoToGame
import ui.NavigationEvent
import ui.ClientEvent
import ui.NewTurn
import model.Player
import ui.NewTurn
import game.Game
import game.Figure
import game.Field

class GamePresenter extends Presenter[GameView] {

	lazy val view = new GameView(this)

	val events = List(NewTurn(Player(""), 0))

	val startEvent = GoToGame(Nil, Player(""))

	var selfPlayer: Player = _

	var game: Game = _

	def onStart = {
		case GoToGame(players, self) => {
			this.selfPlayer = self
			game = Game(players)
			updateUi {
				view.players(players)
				view.showGame(game)
			}
		}
	}

	def on = {
		case NewTurn(player, number) if (player == selfPlayer) => {
			lastDiceNumber = Some(number)
			updateUi {
				view.yourTurn(player)
				view.dice(number)
			}
		}
		case NewTurn(player, number) => {
			lastDiceNumber = None
			updateUi {
				view.newTurn(player)
				view.dice(number)
			}
		}
	}

	def onEnd {}

	var lastDiceNumber: Option[Int] = None

	def previewFigure(player: Player, figure: Figure) {
		if (player == selfPlayer) {
			lastDiceNumber map (dice => view.previewPosition(game.nextPosition(player, figure, dice)))
		}
	}
}