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

case class ConnectedPlayer(player: Player, actor: ActorRef)

case class DisconnectPlayer(player: Player)

case class StartGame(players: List[Player])

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
			val players = connectedPlayers.keys.toList
			sendAll { _ =>
				StartGame(players)
			}
		}
		case x => println("receive on server " + x)
	}

	def sendAll = (event: Player => Any) => {
		val players = connectedPlayers.keys.toList
		connectedPlayers map {
			case (player, actor) => {
				actor ! event(player)
			}
		}
	}

}