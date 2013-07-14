package communication

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import akka.actor.Props
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorRef
import ui.Main
import model.Player
import ui.JoinPlayer

case class ClientMessage(x: Any)

object Client {

	def create(player: Player, remoteServerIp: String = "127.0.0.1"): ActorRef = {
		val system = ClientServer.system
		val server = system.actorFor(
			"akka://ClientServer@" + remoteServerIp + ":2553/user/clientServer")
		val actor = system.actorOf(Props(new Client(server)), "client")

		server ! ConnectedPlayer(player, actor)

		actor
	}
}

class Client(remoteActor: ActorRef) extends Actor with ActorLogging {

	def receive = {
		case ClientMessage(x) => {
			println("sending to server")
			remoteActor ! x
		}
		case event @ StartGame(player) => {
			Main.publish(event)
		}
		case event @ JoinPlayer(player) => {
			Main.publish(event)
		}
		case x => println("receive on client " + x)
	}
}