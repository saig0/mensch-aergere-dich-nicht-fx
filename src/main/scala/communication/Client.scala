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
import game.Figure
import akka.actor.Terminated
import java.nio.channels.ClosedChannelException
import akka.remote.RemoteClientError
import java.net.InetAddress

case class ClientMessage(x: Any)

object Client {

	lazy val system = ActorSystem("Client", ConfigFactory.load.getConfig("client"))

	lazy val localIp = InetAddress.getLocalHost.getHostAddress

	def create(player: Player, remoteServerIp: String = localIp): ActorRef = {
		val server = system.actorFor(
			"akka://ClientServer@" + remoteServerIp + ":2553/user/clientServer")

		ErrorActor(system)

		val actor = system.actorOf(Props(new Client(server)), "client")

		// löst eine Exception aus, wenn keine Verbindung aufgebaut werden kann
		server ! ConnectedPlayer(player, actor)

		this.client = actor
		actor
	}

	var client: ActorRef = _
}

class Client(server: ActorRef) extends Actor with ActorLogging {

	def receive = {
		case ClientMessage(x) => {
			println("sending to server")
			server ! x
		}
		case event @ StartGame(players) => {
			Main.publish(ui.StartGame(players))
		}
		case event @ JoinPlayer(player) => {
			Main.publish(event)
		}
		case event @ StartGame => {
			server ! event
		}
		case event @ NewTurn(player, number) => {
			Main.publish(ui.NewTurn(player, number))
		}
		case ui.MoveFigure(player, figure, number) => {
			server ! MoveFigure(player, figure, number)
		}
		case MoveFigure(player, figure, number) => {
			Main.publish(ui.MoveFigure(player, figure, number))
		}
		case event @ TurnCompleted(_) => server ! event
		case ui.GameEnd(winner) => server ! GameEnd(winner)
		case GameEnd(_) => disconnect
		case event @ ContinueTurn(_) => server ! event
		case x => println("receive on client " + x)
	}

	private def disconnect {
		Client.system.stop(self)
	}
}