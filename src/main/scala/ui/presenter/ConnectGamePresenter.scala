package ui.presenter

import model.Player
import rest.GameServerClient
import model.Game
import communication.Client
import akka.actor.ActorRef
import communication.ConnectedPlayer
import communication.StartGame
import communication.ClientMessage
import ui.view.ConnectGameView
import ui.GoToConnectIp
import ui.Main
import ui.GoToGame
import ui.GoToStart
import scala.concurrent.future

class ConnectGamePresenter extends Presenter[ConnectGameView] {

	lazy val view = new ConnectGameView(this)

	val events = List(GoToConnectIp(Player("")), StartGame(Nil))

	var clientServer: ActorRef = _

	var player: Player = _

	def receive = {
		case GoToConnectIp(player) => {
			this.player = player
			createView
		}
		case StartGame(players) => {
			updateUi(
				Main.publish(GoToGame(players))
			)
		}
		case x => println("received: " + x)
	}

	def joinGame {
		val ip = view.ip
		println("conntect to " + ip)
		updateUi("Verbinde zum Server " + ip, {
			clientServer = Client.create(player, ip)
			clientServer ! ClientMessage("Client is up!")
		})
	}

	def abort {
		publish(GoToStart)
	}
}