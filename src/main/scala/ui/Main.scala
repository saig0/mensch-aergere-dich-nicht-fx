package ui

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.stage.Stage
import scalafx.scene.layout.HBox
import scalafx.scene.text.Text
import scalafx.scene.text.Font
import scalafx.scene.paint.LinearGradient
import scalafx.scene.paint.Stop
import scalafx.scene.effect.DropShadow
import scalafx.geometry.Insets
import scalafx.scene.paint.Color
import scalafx.scene.effect.Reflection
import scalafx.scene.paint.Color.sfxColor2jfx
import scalafx.scene.paint.Stop.sfxStop2jfx
import scalafx.scene.layout.VBox
import akka.actor.ActorSystem
import akka.actor.Props
import scalafx.application.Platform
import ui.view.AbstractScene
import ui.presenter.Presenter
import ui.presenter._
import model.Player
import communication.ClientServer
import javafx.stage.WindowEvent
import communication.Client
import game.Figure
import util.Logging

trait ClientEvent

trait NavigationEvent extends ClientEvent

case class EndEvent extends ClientEvent

case class GoToStart extends NavigationEvent

case class GoToGameCreation(player: Player, local: Boolean) extends NavigationEvent

case class GoToJoinGame(player: Player) extends NavigationEvent

case class GoToConnectIp(player: Player) extends NavigationEvent

case class GoToGame(players: List[Player], self: Player) extends NavigationEvent

case class JoinPlayer(player: Player) extends ClientEvent

case class StartGame(players: List[Player]) extends ClientEvent

case class NewTurn(player: Player, number: Int) extends ClientEvent

case class MoveFigure(player: Player, figure: Figure, number: Int) extends ClientEvent

case class GameEnd(winner: Player) extends ClientEvent

case class ServerConnectionError(cause: Throwable) extends ClientEvent

object Main extends JFXApp with Logging {

	val system = ActorSystem.create("EventBus")

	var activePresenter: Option[Presenter[_]] = None

	stage = new JFXApp.PrimaryStage {
		title = "Mensch-Ärgere-Dich-Nicht-FX"
		width = 1200
		height = 900

		centerOnScreen

		onCloseRequest = closeApplication

	}

	private def closeApplication {
		publish(EndEvent)
		Thread.sleep(1000)
		ClientServer.system.shutdown
		Client.system.shutdown
		system.shutdown
	}

	private def loadSceen[S <: AbstractScene](scene: S) = {
		scene.stylesheets += loadCss
		stage.scene = scene
	}

	def switchToPresenter[V <: AbstractScene](presenter: Presenter[V]) {
		activePresenter map { p =>
			p.onEnd
			p.active = false
		}
		loadSceen(presenter.view)
		activePresenter = Some(presenter)
	}

	private lazy val loadCss =
		getClass().getResource("/default.css").toExternalForm()

	def publish(event: Object) {
		system.eventStream.publish(event)
	}

	def loadPresenter[P <: Presenter[_]](presenter: Class[P]) {
		system.actorOf(Props(presenter))
	}

	loadPresenter(classOf[StartPresenter])
	loadPresenter(classOf[GameCreationPresenter])
	loadPresenter(classOf[JoinGamePresenter])
	loadPresenter(classOf[GamePresenter])
	loadPresenter(classOf[ConnectGamePresenter])

	Thread.sleep(1000)
	publish(GoToStart())

	debug("app started")
}