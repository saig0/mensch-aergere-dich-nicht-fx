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
		subscripe(this, events)
	}
}