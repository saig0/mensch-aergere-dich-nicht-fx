package communication

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import akka.actor.Props
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorRef
import ui.Main
import ui.JoinPlayer
import model.Player
import game._
import scala.concurrent._
import ExecutionContext.Implicits.global
import ui.view.Dice
import ui.view.PlayerFigure

object ComputerPlayer {

	var cpuPlayer = 0

	def create = {
		cpuPlayer += 1
		val server = ClientServer.server
		val player = Player("CPU" + cpuPlayer)
		val actor = ClientServer.system.actorOf(Props(new ComputerPlayer(server, player)), "cpuPlayer_" + cpuPlayer)
		server ! ConnectedPlayer(player, actor)
		actor
	}
}

class ComputerPlayer(server: ActorRef, cpuPlayer: Player) extends Actor with ActorLogging {

	var game: Game = _

	def receive = {
		case DisconnectPlayer => {
			disconnect
		}
		case StartGame(players) => {
			game = Game(players)
		}
		case NewTurn(player, dice) if (player == cpuPlayer) => {
			future {
				// warten bis Würfel fertig ist
				Thread.sleep(Dice.rollAnimationDurationInMillis)

				if (game.canMoveFigure(player, dice)) {
					val figure = GameEvaluation.getBestMove(game, player, dice)
					server ! MoveFigure(player, figure, dice)
				} else {
					future {
						Thread.sleep(Dice.waitAfterRollInMillis)
						game.couldNotMoveFigure(player)
						nextAction(player, dice)
					}
				}
			}
		}
		case NewTurn(player, dice) => {
			if (!game.canMoveFigure(player, dice)) {
				game.couldNotMoveFigure(player)
			}
		}
		case MoveFigure(player, figure, dice) => {
			game.nextPositions(player, figure, dice) map { movement =>
				// warten bis Animation zu ende ist
				Thread.sleep(PlayerFigure.moveAnimationDurationInMillis * movement.size)

				game.moveFigure(player, figure, movement.last) map {
					_ match {
						case BeatFigure(beatenPlayer, figure) => {
							val startPosition = Start(0)
							game.moveFigure(beatenPlayer, figure, startPosition)
							nextAction(player, dice)
						}
						case Win(winPlayer) if (winPlayer == cpuPlayer) => {
							server ! GameEnd(cpuPlayer)
						}
						case _ =>
					}
				} getOrElse nextAction(player, dice)
			}
		}
		case GameEnd(_) => disconnect
		case _ =>
	}

	private def nextAction(player: Player, dice: Int) {
		if (player == cpuPlayer) {
			game.nextAction(player, dice) match {
				case EndTurn() => server ! TurnCompleted(cpuPlayer)
				case RollDiceAgain(_) => server ! ContinueTurn(cpuPlayer)
			}
		}
	}

	override def postStop {
		disconnect
	}

	private def disconnect {
		ComputerPlayer.cpuPlayer -= 1
		ClientServer.system.stop(self)
	}
}