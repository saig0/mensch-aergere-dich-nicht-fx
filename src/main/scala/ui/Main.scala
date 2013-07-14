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
import model.Player

case class StartEvent

case class EndEvent

case class GoToStart

case class GoToGameCreation(player: Player)

case class GoToJoinGame(player: Player)

case class GoToConnectIp(player: Player)

case class GoToGame(players: List[Player])

case class JoinPlayer(player: Player)

object Main extends JFXApp {

	val system = ActorSystem.create("EventBus")

	stage = new JFXApp.PrimaryStage {
		title = "Mensch-Ärgere-Dich-Nicht-FX"
		width = 1200
		height = 800

		centerOnScreen

		onCloseRequest = {
			publish(EndEvent)
			system.shutdown
		}
	}

	def loadSceen[S <: AbstractScene](scene: S) = {
		scene.stylesheets += loadCss
		stage.scene = scene
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
	publish(StartEvent)
}