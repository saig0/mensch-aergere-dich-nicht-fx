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
			ComputerPlayer.cpuPlayer -= 1
			Main.system.stop(self)
		}
		case StartGame(players) => {
			game = Game(players)
		}
		case NewTurn(player, dice) => {
			if (player == cpuPlayer) {
				// warten bis Würfel fertig ist
				Thread.sleep(2000)
				// TODO: intelligentere Auswahl der Figur
				val figure = game.gameStates(player).figures.head
				server ! MoveFigure(player, figure, dice)
			}
		}
		case MoveFigure(player, figure, dice) => {
			game.nextPositions(player, figure, dice) map { movement =>
				game.moveFigure(player, figure, movement.last) map {
					_ match {
						case BeatFigure(player, figure) => {
							val startPosition = Start(0)
							game.moveFigure(player, figure, startPosition)
						}
					}
				}
			}
		}
		case x => println("receive on cpu " + x)
	}
}