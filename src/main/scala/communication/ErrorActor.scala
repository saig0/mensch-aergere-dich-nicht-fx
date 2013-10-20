package communication

import akka.actor.Actor
import akka.actor.Terminated
import ui.Main
import akka.actor.Props
import akka.actor.ActorRef
import akka.remote._
import akka.actor.ActorSystem

object ErrorActor {
	def apply(system: ActorSystem) {
		val errorActor = system.actorOf(Props[ErrorActor])
		system.eventStream.subscribe(errorActor, classOf[RemoteLifeCycleEvent])
	}
}

class ErrorActor extends Actor {
	def receive = {
		case e: RemoteClientError => println(">>>>>>>> " + e)
		case e => println("-------- " + e)
	}
}