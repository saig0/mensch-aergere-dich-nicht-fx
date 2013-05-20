package ui

import scala.concurrent.Future
import scala.util.Success
import scala.util.Failure
import scalafx.application.Platform
import dispatch._
import Defaults._

trait Presenter[V <: AbstractScene] {

	val view: V

	def createView {
		Main.loadSceen(view)
	}

	def updateUi[T](future: Future[T], onSuccess: T => Unit, onFailure: Throwable => Unit = (t => t.printStackTrace)) {
		future onComplete {
			case Success(result) => Platform.runLater(onSuccess(result))
			case Failure(failure) => Platform.runLater(onFailure(failure))
		}
	}
}