package communication

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import akka.actor.Props
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorRef
import ui.Main
import ui.JoinPlayer
import model.Player

object ComputerPlayer {

	var cpuPlayer = 0

	def create = {
		cpuPlayer += 1
		val actor = Main.system.actorOf(Props[ComputerPlayer], "cpuPlayer_" + cpuPlayer)
		Main.publish(JoinPlayer(Player("CPU" + cpuPlayer)))
		actor
	}
}

class ComputerPlayer extends Actor with ActorLogging {

	def receive = {
		case x => println("receive on cpu " + x)
	}
}