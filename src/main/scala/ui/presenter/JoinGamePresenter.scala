package ui.presenter

import model.Player
import rest.GameServerClient
import model.Game
import communication.Client
import akka.actor.ActorRef
import communication.ConnectedPlayer
import communication.StartGame
import ui.view.JoinGameView
import ui.GoToJoinGame
import ui.Main
import ui.GoToStart
import ui.GoToGame
import ui.JoinPlayer
import ui.JoinPlayer
import model.Player

class JoinGamePresenter extends Presenter[JoinGameView] {

	lazy val view = new JoinGameView(this)

	val events = List(GoToJoinGame(Player("")), JoinPlayer(Player("")), StartGame(Nil))

	var clientServer: ActorRef = _

	var player: Player = _

	def receive = {
		case GoToJoinGame(player) => {
			this.player = player
			createView
			loadGames
		}
		case StartGame(players) => {
			updateUi(
				Main.publish(GoToGame(players))
			)
		}
		case JoinPlayer(joinedPlayer) if (joinedPlayer == player) => {
			updateUi {
				view.showLoading("Verbunden mit Server. Warte auf Spiel Start.")
			}
		}
	}

	def joinGame {
		val game = view.selectedGame
		println("join " + game)
		updateUi("Verbinde zum Server " + game.address, {
			clientServer = Client.create(player, game.address)
			clientServer ! "Client is up!"
		})
	}

	def loadGames {
		updateUi("Lade Spiele vom Server", GameServerClient.getAvaiableGames, { games: List[Game] =>
			view.showGames(games)
		})
	}

	def abort {
		publish(GoToStart)
	}
}