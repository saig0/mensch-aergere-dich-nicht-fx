package communication

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import akka.actor.Props
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorRef
import ui.Main

case class ClientMessage(x: Any)

object Client {

	def create(remoteServerIp: String) = {
		val system =
			ActorSystem("Client", ConfigFactory.load.getConfig("client"))
		val remoteActor = system.actorFor(
			"akka://ClientServer@" + remoteServerIp + ":2553/user/clientServer")
		val actor = system.actorOf(Props(new Client(remoteActor)), "client")

		actor
	}
}

class Client(remoteActor: ActorRef) extends Actor with ActorLogging {

	def send(x: Any) {
		println("sending to server")
		remoteActor ! x
	}

	def receive = {
		case (actor: ActorRef, x: Any) => {
			println("sending to server")
			actor ! x
		}
		case ClientMessage(x) => {
			println("sending to server")
			remoteActor ! x
		}
		case event @ StartGame(player) => {
			Main.publish(event)
		}
		case x => println("receive on client " + x)
	}
}