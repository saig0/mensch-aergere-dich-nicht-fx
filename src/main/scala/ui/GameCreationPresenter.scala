package ui

import rest.GameServerClient
import model.Game
import model.Player
import model.Player
import communication.ClientServer
import akka.actor.ActorRef

class GameCreationPresenter extends Presenter[GameCreationView] {

	lazy val view = new GameCreationView(this)

	val events = List(GoToGameCreation(Player("")), EndEvent)

	var game: Option[Game] = None

	var clientServer: ActorRef = _

	def receive = {
		case GoToGameCreation(player) => {
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
			view.joinPlayer(player)
		}
		case EndEvent => {
			game map (GameServerClient.deleteGame(_))
		}
	}

	def abort {
		game map (GameServerClient.deleteGame(_))
		publish(GoToStart)
	}
}