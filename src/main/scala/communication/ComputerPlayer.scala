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

	def create(server: ActorRef) = {
		cpuPlayer += 1
		val actor = Main.system.actorOf(Props(new ComputerPlayer(server)), "cpuPlayer_" + cpuPlayer)
		val player = Player("CPU" + cpuPlayer)
		server ! ConnectedPlayer(player, actor)
		actor
	}
}

class ComputerPlayer(server: ActorRef) extends Actor with ActorLogging {

	def receive = {
		case DisconnectPlayer => {
			println("disconnect cpu")
			ComputerPlayer.cpuPlayer -= 1
			Main.system.stop(self)
		}
		case x => println("receive on cpu " + x)
	}
}