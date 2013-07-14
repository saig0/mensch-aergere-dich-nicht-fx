package ui.presenter

import rest.GameServerClient
import model.Game
import model.Player
import model.Player
import communication.ClientServer
import akka.actor.ActorRef
import communication.ComputerPlayer
import model.Player
import communication.DisconnectPlayer
import communication.StartGame
import communication.ConnectedPlayer
import communication.StartGame
import model.Player
import communication.StartGame
import communication.Client
import ui.view.GameCreationView
import ui.GoToGameCreation
import ui.EndEvent
import ui.JoinPlayer
import ui.Main
import ui.GoToGame
import ui.GoToStart

class GameCreationPresenter extends Presenter[GameCreationView] {

	lazy val view = new GameCreationView(this)

	val events = List(GoToGameCreation(Player("")), EndEvent, JoinPlayer(Player("")), StartGame(Nil))

	var game: Option[Game] = None

	var client: ActorRef = _

	var player: Player = _

	def receive = {
		case GoToGameCreation(player) => {
			this.player = player
			createView
			updateUi("Erstelle Spiel auf Server", GameServerClient.newGame(4, 0), { (game: Game) =>
				this.game = Some(game)
				view.showGame(game)

				val server = ClientServer.create
				server ! "Server is up!"
				client = Client.create(player)
				client ! "Client of Server is up!"
			})
		}
		case JoinPlayer(player) => {
			game = game map (g => g.copy(currentPlayers = g.currentPlayers + 1))
			updateUi(GameServerClient.updateGame(game.get), { _: Unit =>
				view.joinPlayer(player)
			})
		}
		case EndEvent => {
			game map (GameServerClient.deleteGame(_))
		}
		case StartGame(players) => {
			updateUi("Entferne Spiel vom Server", GameServerClient.deleteGame(game.get), { _: Unit =>
				Main.publish(GoToGame(players))
			})
		}
	}

	def newCpuPlayer {
		ComputerPlayer.create
	}

	def removePlayer {
		val player = view.selectedPlayer
		if (player != this.player) {
			client
			game = game map (g => g.copy(currentPlayers = g.currentPlayers - 1))
			updateUi(GameServerClient.updateGame(game.get), { _: Unit =>
				view.removePlayer(player)
			})
		}
	}

	def startGame {
		val game = this.game.get
		if (game.currentPlayers == game.maxPlayers) {
			client ! StartGame
		}
	}

	def abort {
		game map (GameServerClient.deleteGame(_))
		publish(GoToStart)
	}
}