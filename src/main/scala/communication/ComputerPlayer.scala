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
		case NewTurn(player, dice) => {
			if (player == cpuPlayer) {
				future {
					// warten bis Würfel fertig ist
					Thread.sleep(1000 * 2)

					if (game.canMoveFigure(player, dice)) {
						// TODO: intelligentere Auswahl der Figur
						val figure = game.gameStates(player).figures.head
						server ! MoveFigure(player, figure, dice)
					} else {
						future {
							Thread.sleep(1000)
							nextAction(player, dice)
						}
					}
				}
			}
		}
		case MoveFigure(player, figure, dice) => {
			game.nextPositions(player, figure, dice) map { movement =>
				// warten bis Animation zu ende ist
				Thread.sleep(1000 * dice)

				game.moveFigure(player, figure, movement.last) map {
					_ match {
						case BeatFigure(player, figure) => {
							val startPosition = Start(0)
							game.moveFigure(player, figure, startPosition)
							nextAction(player, dice)
						}
					}
				} getOrElse nextAction(player, dice)
			}
		}
		case GameEnd(_) => disconnect
		case x => println("receive on cpu " + x)
	}

	private def nextAction(player: Player, dice: Int) {
		if (player == cpuPlayer) {
			game.nextAction(player, dice) match {
				case EndTurn() => server ! TurnCompleted(cpuPlayer)
				case RollDiceAgain(_) => server ! ContinueTurn(cpuPlayer)
			}
		}
	}

	private def disconnect {
		ComputerPlayer.cpuPlayer -= 1
		ClientServer.system.stop(self)
	}
}