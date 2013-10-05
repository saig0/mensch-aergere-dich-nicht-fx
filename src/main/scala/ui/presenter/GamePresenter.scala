package ui.presenter

import ui.view.GameView
import ui.GoToGame
import ui.NavigationEvent
import ui.ClientEvent
import model.Player
import ui.NewTurn
import ui.MoveFigure
import game._
import ui.MoveFigure
import communication.ClientServer
import akka.actor.ActorRef
import communication.Client

class GamePresenter extends Presenter[GameView] {

	lazy val view = new GameView(this)

	val events = List(NewTurn(Player(""), 0), MoveFigure(Player(""), Figure(Start(0)), 0))

	val startEvent = GoToGame(Nil, Player(""))

	var selfPlayer: Player = _

	var game: Game = _

	lazy val client = Client.client

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
		case MoveFigure(player, figure, dice) => {
			val movement = game.nextPositions(player, figure, dice)
			view.moveFigure(player, figure, movement)
			game.moveFigure(player, figure, movement.last)
		}
	}

	def onEnd {}

	var lastDiceNumber: Option[Int] = None

	def previewFigure(player: Player, figure: Figure) {
		if (player == selfPlayer) {
			lastDiceNumber map { dice =>
				val movement = game.nextPositions(player, figure, dice)
				view.previewPositions(player, movement)
			}
		}
	}

	def removePreviewFigure {
		view.removePreviewFigure
	}

	def moveFigure(player: Player, figure: Figure) {
		if (player == selfPlayer) {
			lastDiceNumber map { dice =>
				client ! MoveFigure(player, figure, dice)
			}
		}
	}
}