package communication

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import akka.actor.Props
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorRef

object Client {

	def create(remoteServerIp: String) = {
		val system =
			ActorSystem("Client", ConfigFactory.load.getConfig("client"))
		val actor = system.actorOf(Props[Client], "client")
		val remoteActor = system.actorFor(
			"akka://ClientServer@" + remoteServerIp + ":2553/user/clientServer")
		remoteActor
	}
}

class Client extends Actor with ActorLogging {

	def receive = {
		case (actor: ActorRef, x: Any) => {
			println("sending to server")
			actor ! x
		}
		case x => println("receive on client " + x)
	}
}