package ui

import scala.concurrent.Future
import scala.util.Success
import scala.util.Failure
import scalafx.application.Platform
import dispatch._
import Defaults._
import akka.actor.Actor
import akka.actor.Props

trait Presenter[V <: AbstractScene] extends Actor {

	val view: V

	val events: List[Any]

	def createView {
		Platform.runLater {
			Main.loadSceen(view)
		}
	}

	def updateUi[T](future: Future[T], onSuccess: T => Unit, onFailure: Throwable => Unit = (t => t.printStackTrace)) {
		future onComplete {
			case Success(result) => Platform.runLater(onSuccess(result))
			case Failure(failure) => Platform.runLater(onFailure(failure))
		}
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
		subscripe(this, events)
	}
}