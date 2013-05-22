package communication

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import akka.actor.Props
import akka.actor.Actor
import akka.actor.ActorLogging

object ClientServer {

	def create = {
		val system = ActorSystem("ClientServer",
			ConfigFactory.load.getConfig("clientServer"))
		val actor = system.actorOf(Props[ClientServer], name = "clientServer")
		actor
	}

}

class ClientServer extends Actor with ActorLogging {

	def receive = {
		case x => println("receive on server " + x)
	}
}