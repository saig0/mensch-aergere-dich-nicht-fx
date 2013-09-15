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

	lazy val system = ActorSystem("Client", ConfigFactory.load.getConfig("client"))

	def create(player: Player, remoteServerIp: String = "127.0.0.1"): ActorRef = {
		val server = system.actorFor(
			"akka://ClientServer@" + remoteServerIp + ":2553/user/clientServer")
		val actor = system.actorOf(Props(new Client(server)), "client")

		// l�st eine Exception aus, wenn keine Verbindung aufgebaut werden kann
		server ! ConnectedPlayer(player, actor)

		actor
	}
}

class Client(server: ActorRef) extends Actor with ActorLogging {

	def receive = {
		case ClientMessage(x) => {
			println("sending to server")
			server ! x
		}
		case event @ StartGame(player) => {
			Main.publish(event)
		}
		case event @ JoinPlayer(player) => {
			Main.publish(event)
		}
		case event @ StartGame => {
			server ! event
		}
		case x => println("receive on client " + x)
	}
}