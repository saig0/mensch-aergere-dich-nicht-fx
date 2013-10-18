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
import scala.concurrent._
import ExecutionContext.Implicits.global
import communication.TurnCompleted
import ui.GameEnd
import communication.ContinueTurn
import communication.TurnCompleted

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
			if (!game.canMoveFigure(player, number)) {
				future {
					Thread.sleep(1000 * (2 + 1)) // warten auf Würfel Animation 
					game.couldNotMoveFigure(player)
					nextAction(player, number)
				}
			}
		}
		case NewTurn(player, number) => {
			lastDiceNumber = None
			updateUi {
				view.newTurn(player)
				view.dice(number)
			}
			if (!game.canMoveFigure(player, number)) {
				game.couldNotMoveFigure(player)
			}
		}
		case MoveFigure(player, figure, dice) => {
			game.nextPositions(player, figure, dice) map { movement =>
				view.moveFigure(player, figure, movement)
				future {
					// warten bis Animation zu ende ist
					Thread.sleep(1000 * movement.size)

					game.moveFigure(player, figure, movement.last) map {
						_ match {
							case BeatFigure(beatenPlayer, figure) => {
								val startPosition = Start(0)
								view.moveFigure(beatenPlayer, figure, List(startPosition))
								game.moveFigure(beatenPlayer, figure, startPosition)
								nextAction(player, dice)
							}
							case Win(player) => {
								if (player == selfPlayer) {
									view.youWin(selfPlayer)
									client ! GameEnd(selfPlayer)
								} else {
									view.playerWin(player)
								}
							}
						}
					} getOrElse nextAction(player, dice)
				}
			}
		}
	}

	private def nextAction(player: Player, dice: Int) =
		if (player == selfPlayer) {
			game.nextAction(player, dice) match {
				case EndTurn() => client ! TurnCompleted(selfPlayer)
				case RollDiceAgain(player) => client ! ContinueTurn(selfPlayer)
			}
		}

	def onEnd {}

	var lastDiceNumber: Option[Int] = None

	def previewFigure(player: Player, figure: Figure) {
		if (player == selfPlayer) {
			lastDiceNumber map { dice =>
				game.nextPositions(player, figure, dice) map (movement =>
					view.previewPositions(player, movement))
			}
		}
	}

	def removePreviewFigure {
		view.removePreviewFigure
	}

	def moveFigure(player: Player, figure: Figure) {
		if (player == selfPlayer) {
			lastDiceNumber map { dice =>
				game.nextPositions(player, figure, dice) map (_ =>
					client ! MoveFigure(player, figure, dice))
			}
		}
	}
}