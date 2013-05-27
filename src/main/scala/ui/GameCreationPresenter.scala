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
import communication.StartGame
import communication.ConnectedPlayer
import communication.StartGame
import model.Player
import communication.StartGame

class GameCreationPresenter extends Presenter[GameCreationView] {

	lazy val view = new GameCreationView(this)

	val events = List(GoToGameCreation(Player("")), EndEvent, JoinPlayer(Player("")), StartGame(List[Player]()))

	var game: Option[Game] = None

	var clientServer: ActorRef = _

	var player: Player = _

	def receive = {
		case GoToGameCreation(player) => {
			this.player = player
			createView
			updateUi(GameServerClient.newGame(4, 0), { (game: Game) =>
				this.game = Some(game)
				view.showGame(game)

				clientServer = ClientServer.create
				clientServer ! "Server is up!"
				clientServer ! ConnectedPlayer(player, self)
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
			println("start game" + players)
		}
	}

	def newCpuPlayer {
		ComputerPlayer.create(clientServer)
	}

	def removePlayer {
		val player = view.selectedPlayer
		if (player != this.player) {
			clientServer
			game = game map (g => g.copy(currentPlayers = g.currentPlayers - 1))
			updateUi(GameServerClient.updateGame(game.get), { _: Unit =>
				view.removePlayer(player)
			})
		}
	}

	def startGame {
		val game = this.game.get
		if (game.currentPlayers == game.maxPlayers) {
			clientServer ! StartGame
		}
	}

	def abort {
		game map (GameServerClient.deleteGame(_))
		publish(GoToStart)
	}
}