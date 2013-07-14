package ui.view

import javafx.event.Event
import javafx.event.EventHandler
import javafx.collections.ObservableList
import java.util.LinkedList
import javafx.collections.FXCollections
import scalafx.scene.control.TableColumn
import scalafx.beans.property.ObjectProperty
import scalafx.scene.Node

trait ScalaFxView {

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

	implicit def propertyToObservable[T](x: T): ObjectProperty[T] =
		ObjectProperty(this, "", x)
}