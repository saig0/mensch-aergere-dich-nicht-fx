package ui.presenter

import model.Player
import rest.GameServerClient
import model.Game
import communication.Client
import akka.actor.ActorRef
import communication.ConnectedPlayer
import communication.ClientMessage
import ui.view.ConnectGameView
import ui.GoToConnectIp
import ui.Main
import ui.GoToGame
import ui.GoToStart
import scala.concurrent.future
import ui.JoinPlayer
import ui.JoinPlayer
import ui.GoToConnectIp
import ui.StartGame
import ui.ServerConnectionError
import ui.ServerConnectionError
import akka.actor.Kill

class ConnectGamePresenter extends Presenter[ConnectGameView] {

	lazy val view = new ConnectGameView(this)

	val events = List(JoinPlayer(Player("")), StartGame(Nil), ServerConnectionError(new Throwable))

	var client: Option[ActorRef] = None

	var player: Player = _

	val startEvent = GoToConnectIp(Player(""))

	def onStart = {
		case GoToConnectIp(player) => {
			this.player = player
			updateUi {
				view.showView
				view.ip = Client.localIp
			}
		}
	}

	def onEnd {}

	def on = {
		case StartGame(players) => {
			updateUi(
				Main.publish(GoToGame(players, player))
			)
		}
		case JoinPlayer(joinedPlayer) if (joinedPlayer == player) => {
			updateUi {
				view.showLoading("Verbunden mit " + view.ip + ". Warte auf Spiel Start.")
			}
		}
		case ServerConnectionError(cause) => {
			updateUi {
				view.failedToConnect(cause.getMessage())
			}
			client map (_ ! Kill)
		}
		case _ =>
	}

	def joinGame {
		val ip = view.ip
		updateUi("Verbinde zum Server " + ip, {
			val client = Client.create(player, ip)
			client ! ClientMessage("Client is up!")
			this.client = Some(client)
		})
	}

	def abort {
		client map (_ ! Kill)
		publish(GoToStart())
	}
}