package ui.view

import scalafx.scene.Scene
import scalafx.scene.layout._
import scalafx.geometry.Insets
import scalafx.scene.text.Text
import scalafx.scene.effect._
import scalafx.scene.paint._
import scalafx.geometry.Pos
import javafx.event.EventHandler
import javafx.event.Event
import scalafx.scene.Node
import dispatch._
import dispatch.Defaults._
import javafx.collections.ObservableList
import javafx.collections.FXCollections
import java.util.LinkedList
import scalafx.scene.control.TableColumn
import scalafx.beans.property.ObjectProperty
import scalafx.scene.paint.Stop.sfxStop2jfx

abstract class AbstractScene extends Scene {

	content = new BorderPane {

		top = new HBox {
			padding = Insets(20)
			alignment = Pos.CENTER
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
				alignment = Pos.CENTER

				content = contentCenter
			}

	}

	def contentCenter: List[Node]

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

	implicit def propertyToObservable[T](x: T): ObjectProperty[T] = ObjectProperty(this, "", x)

}