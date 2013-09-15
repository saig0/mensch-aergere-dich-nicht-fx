package ui.presenter

import scala.concurrent.Future
import scala.concurrent.future
import scala.util.Success
import scala.util.Failure
import scalafx.application.Platform
import dispatch._
import dispatch.Defaults._
import akka.actor.Actor
import ui.Main
import ui.view.AbstractScene
import ui.NavigationEvent
import ui.ClientEvent
import ui.NavigationEvent
import ui.NavigationEvent
import ui.NavigationEvent
import ui.NavigationEvent
import ui.NavigationEvent
import ui.NavigationEvent
import ui.EndEvent
import ui.EndEvent
import ui.ClientEvent
import ui.GoToStart
import ui.GoToGameCreation
import model.Player
import ui.GoToJoinGame
import model.Player
import ui.GoToConnectIp
import ui.GoToGame
import ui.NavigationEvent
import ui.NavigationEvent

trait Presenter[V <: AbstractScene] extends Actor {

	val view: V

	val events: List[ClientEvent]

	def createView {
		Platform.runLater {
			Main.loadSceen(view)
		}
	}

	def updateUi[T](future: Future[T], onSuccess: T => Unit, onFailure: Throwable => Unit = (t => t.printStackTrace)) {
		future onComplete {
			case Success(result) => updateUi(onSuccess(result))
			case Failure(failure) => updateUi(onFailure(failure))
		}
	}

	def updateUi[T](loadingText: String, future: Future[T], onSuccess: T => Unit, onFailure: Throwable => Unit = (t => t.printStackTrace)) {
		updateUi(view.showLoading(loadingText))
		updateUi(future, onSuccess, onFailure)
	}

	def updateUi(loadingText: String, f: => Unit) {
		view.showLoading(loadingText)
		future(updateUi(f))
	}

	def updateUi(f: => Unit) {
		Platform.runLater(f)
	}

	def subscripe(presenter: Presenter[_], events: List[Any]) {
		events map { event =>
			Main.system.eventStream.subscribe(presenter.self, event.getClass)
		}
	}

	def publish(event: Object) {
		Main.publish(event)
	}

	override def preStart {
		subscripe(this, events ::: navigationEvents ::: EndEvent :: Nil)
	}

	val navigationEvents: List[NavigationEvent] = List(GoToStart(), GoToGameCreation(Player("")), GoToJoinGame(Player("")), GoToConnectIp(Player("")), GoToGame(Nil))

	val startEvent: NavigationEvent

	def onStart: NavigationEvent => Unit

	def onEnd: Unit

	var active = false

	def receive = {
		case event: NavigationEvent if (event.getClass == startEvent.getClass) => {
			active = true
			createView
			onStart(event)
		}
		case event: NavigationEvent if (active) => {
			active = false
			onEnd
		}
		case event: EndEvent if (active) => {
			active = false
			onEnd
		}
		case event: ClientEvent if (active) => on(event)
	}

	def on: ClientEvent => Unit
}