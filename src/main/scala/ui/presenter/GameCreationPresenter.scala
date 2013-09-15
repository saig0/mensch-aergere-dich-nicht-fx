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
import communication.ConnectedPlayer
import model.Player
import communication.Client
import ui.view.GameCreationView
import ui.GoToGameCreation
import ui.EndEvent
import ui.JoinPlayer
import ui.Main
import ui.GoToGame
import ui.GoToStart
import ui.GoToGameCreation
import ui.StartGame

class GameCreationPresenter extends Presenter[GameCreationView] {

	lazy val view = new GameCreationView(this)

	val events = List(JoinPlayer(Player("")), StartGame(Nil))

	var game: Option[Game] = None

	var client: ActorRef = _

	var player: Player = _

	val startEvent = GoToGameCreation(Player(""))

	def onStart = {
		case GoToGameCreation(player) => {
			this.player = player
			updateUi("Erstelle Spiel auf Server", GameServerClient.newGame(4, 0), { (game: Game) =>
				this.game = Some(game)
				view.showGame(game)

				val server = ClientServer.create
				server ! "Server is up!"
				client = Client.create(player)
				client ! "Client of Server is up!"
			})
		}
	}

	def onEnd {
		game map (GameServerClient.deleteGame(_))
	}

	def on = {
		case JoinPlayer(player) => {
			game = game map (g => g.copy(currentPlayers = g.currentPlayers + 1))
			game map { g =>
				updateUi(GameServerClient.updateGame(g), { _: Unit =>
					view.joinPlayer(player)
				})
			}
		}
		case StartGame(players) => {
			game map { g =>
				updateUi("Entferne Spiel vom Server", GameServerClient.deleteGame(g), { _: Unit =>
					Main.publish(GoToGame(players))
				})
			}
		}
	}

	def onStart(event: GoToGameCreation) {
		this.player = event.player
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
			client ! communication.StartGame
		}
	}

	def abort {
		game map (GameServerClient.deleteGame(_))
		publish(GoToStart())
	}
}