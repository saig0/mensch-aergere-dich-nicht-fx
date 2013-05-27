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

case class ConnectedPlayer(player: Player, actor: ActorRef)

case class DisconnectPlayer(player: Player)

object ClientServer {

	def create = {
		val system = ActorSystem("ClientServer",
			ConfigFactory.load.getConfig("clientServer"))
		val actor = system.actorOf(Props[ClientServer], name = "clientServer")
		actor
	}

}

class ClientServer extends Actor with ActorLogging {

	var connectedPlayers = Map[Player, ActorRef]()

	def receive = {
		case ConnectedPlayer(player, actor) => {
			connectedPlayers += (player -> actor)
			Main.publish(JoinPlayer(player))
		}
		case DisconnectPlayer(player) => {
			connectedPlayers(player) ! DisconnectPlayer
			connectedPlayers -= player
		}
		case x => println("receive on server " + x)
	}
}