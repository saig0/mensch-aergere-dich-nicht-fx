package communication

import akka.actor.Actor
import akka.actor.Terminated
import ui.Main
import akka.actor.Props
import akka.actor.ActorRef
import akka.remote._
import akka.actor.ActorSystem
import ui.ServerConnectionError
import util.Logging

object ErrorActor {
	def apply(system: ActorSystem) {
		val errorActor = system.actorOf(Props[ErrorActor])
		system.eventStream.subscribe(errorActor, classOf[RemoteLifeCycleEvent])
	}
}

class ErrorActor extends Actor with Logging {
	def receive = {
		case e: RemoteClientError => Main.publish(ServerConnectionError(e.cause))
		case e => debug("remote life cycle event: " + e)
	}
}