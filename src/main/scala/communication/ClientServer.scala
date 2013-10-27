package communication

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import akka.actor.Props
import akka.actor.Actor
import akka.actor.ActorLogging
import model.Player
import akka.actor.ActorRef
import ui.Main
import ui.JoinPlayer
import ui.EndEvent
import game.Figure
import akka.actor.Kill
import ui.view.Dice.random

trait ServerEvent

case class ConnectedPlayer(player: Player, actor: ActorRef) extends ServerEvent

case class DisconnectPlayer(player: Player) extends ServerEvent

case class StartGame(players: List[Player]) extends ServerEvent

case class NewTurn(player: Player, number: Int) extends ServerEvent

case class TurnCompleted(player: Player) extends ServerEvent

case class ContinueTurn(player: Player) extends ServerEvent

case class MoveFigure(player: Player, figure: Figure, number: Int) extends ServerEvent

case class GameEnd(winner: Player) extends ServerEvent

object ClientServer {

	lazy val system: ActorSystem = ActorSystem("ClientServer", ConfigFactory.load.getConfig("clientServer"))

	def create = {
		val actor = system.actorOf(Props[ClientServer], name = "clientServer")
		server = actor
		actor
	}

	var server: ActorRef = _

}

class ClientServer extends Actor with ActorLogging {

	var connectedPlayers = Map[Player, ActorRef]()

	lazy val players = connectedPlayers.keys.toList

	def receive = {
		case ConnectedPlayer(player, actor) => {
			connectedPlayers += (player -> actor)
			sendAll { _ =>
				JoinPlayer(player)
			}
		}
		case DisconnectPlayer(player) => {
			connectedPlayers(player) ! DisconnectPlayer
			connectedPlayers -= player
		}
		case StartGame => {
			sendAll { _ =>
				StartGame(players)
			}
			// warten bis alle gestartet sind?
			Thread.sleep(1000)
			newTurn(players.head)
		}
		case event @ MoveFigure(player, _, dice) => {
			sendAll(_ => event)
		}
		case TurnCompleted(player) => newTurn(nextPlayer(player))
		case ContinueTurn(player) => newTurn(player)
		case event @ GameEnd(_) => {
			sendAll(_ => event)
			ClientServer.system.stop(self)
			ClientServer.system.shutdown
		}
		case _ =>
	}

	private def nextPlayer(player: Player) = {
		val i = (players.indexOf(player) + 1) % players.size
		players(i)
	}

	private def newTurn(player: Player) {
		val number = random
		val turn = NewTurn(player, number)
		sendAll { _ => turn }
	}

	private def sendAll = (event: Player => Any) => {
		val players = connectedPlayers.keys.toList
		connectedPlayers map {
			case (player, actor) => {
				actor ! event(player)
			}
		}
	}

	override def postStop {
		connectedPlayers map { case (player, actor) => actor ! Kill }
	}

}