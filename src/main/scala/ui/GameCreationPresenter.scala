package ui

import rest.GameServerClient
import model.Game
import model.Player
import model.Player
import communication.ClientServer
import akka.actor.ActorRef
import communication.ComputerPlayer
import model.Player
import communication.DisconnectPlayer

class GameCreationPresenter extends Presenter[GameCreationView] {

	lazy val view = new GameCreationView(this)

	val events = List(GoToGameCreation(Player("")), EndEvent, JoinPlayer(Player("")))

	var game: Option[Game] = None

	var clientServer: ActorRef = _

	var player: Player = _

	def receive = {
		case GoToGameCreation(player) => {
			this.player = player
			createView
			updateUi(GameServerClient.newGame(4, 1), { (game: Game) =>
				this.game = Some(game)
				view.showGame(game)
				view.joinPlayer(player)
			})
			clientServer = ClientServer.create
			clientServer ! "Server is up!"
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
	}

	def newCpuPlayer {
		ComputerPlayer.create(clientServer)
	}

	def removePlayer {
		val player = view.selectedPlayer
		if (player != this.player) {
			clientServer ! DisconnectPlayer(player)
			game = game map (g => g.copy(currentPlayers = g.currentPlayers - 1))
			updateUi(GameServerClient.updateGame(game.get), { _: Unit =>
				view.removePlayer(player)
			})
		}
	}

	def abort {
		game map (GameServerClient.deleteGame(_))
		publish(GoToStart)
	}
}