package ui

import scalafx.scene.Scene
import scalafx.scene.layout._
import scalafx.geometry.Insets
import scalafx.scene.text.Text
import scalafx.scene.effect._
import scalafx.scene.paint._
import scalafx.scene.control.Label
import scalafx.scene.control.TextField
import scalafx.scene.control.Button
import scalafx.geometry.Pos
import javafx.event.EventHandler
import javafx.event.ActionEvent
import javafx.event.Event
import scalafx.scene.Node
import scala.concurrent.Future
import scalafx.application.Platform
import dispatch._
import Defaults._
import scala.util.Success
import scala.util.Failure
import javafx.collections.ObservableList
import javafx.collections.FXCollections
import java.util.LinkedList
import scalafx.scene.control.TableColumn
import scalafx.beans.property.ObjectProperty

abstract class AbstractScene extends Scene {

	content = new BorderPane {

		top = new HBox {
			padding = Insets(20)

			content = List(
				new Text {
					text = "Mensch ärgere dich nicht"
					id = "h1"
					effect = new Reflection
					fill = new LinearGradient(
						endX = 0,
						stops = List(Stop(0, Color.ORANGE), Stop(1, Color.CHOCOLATE)))
				},
				new Text {
					text = " FX"
					id = "h1"
					fill = new LinearGradient(
						endX = 0,
						stops = List(Stop(0, Color.CYAN), Stop(1, Color.DODGERBLUE)))
					effect = new Reflection {
						effect = new DropShadow {
							color = Color.DODGERBLUE
							radius = 25
							spread = 0.25
						}
					}
				}
			)
		}

		center =
			new VBox {
				spacing = 25
				padding = Insets(50)

				content = contentCenter
			}

	}

	def contentCenter: List[Node]

	def updateUi[T](future: Future[T], onSuccess: T => Unit, onFailure: Throwable => Unit = (t => t.printStackTrace)) {
		future onComplete {
			case Success(result) => Platform.runLater(onSuccess(result))
			case Failure(failure) => Platform.runLater(onFailure(failure))
		}
	}

	implicit def actionDsl[T <: Event](f: T => Unit): EventHandler[T] =
		new EventHandler[T] {
			def handle(event: T) {
				f(event)
			}
		}

	implicit def list2Observable[T](list: List[T]): ObservableList[T] = {
		val l = new LinkedList[T]
		list map l.add
		FXCollections.observableArrayList(l)
	}

	implicit def tableColumn2jfx[M, R](column: TableColumn[M, R]) =
		TableColumn.sfxTableColumn2jfx(column)

	implicit def x[T](x: T): ObjectProperty[T] = ObjectProperty(this, "", x)

}