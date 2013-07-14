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

class JoinGamePresenter extends Presenter[JoinGameView] {

	lazy val view = new JoinGameView(this)

	val events = List(GoToJoinGame(Player("")), StartGame(Nil))

	var clientServer: ActorRef = _

	var player: Player = _

	def receive = {
		case GoToJoinGame(player) => {
			this.player = player
			createView
			updateUi(GameServerClient.getAvaiableGames, { games: List[Game] =>
				view.showGames(games)
			})
		}
		case StartGame(players) => {
			updateUi(
				Main.publish(GoToGame(players))
			)
		}
	}

	def joinGame {
		val game = view.selectedGame
		println("join " + game)
		clientServer = Client.create(game.address)
		clientServer ! "Client is up!"
		clientServer ! ConnectedPlayer(player, self)
	}

	def loadGames {
		view.showLoading
		updateUi(GameServerClient.getAvaiableGames, { games: List[Game] =>
			view.showGames(games)
		})
	}

	def abort {
		publish(GoToStart)
	}
}