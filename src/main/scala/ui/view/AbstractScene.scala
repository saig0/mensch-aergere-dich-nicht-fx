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

abstract class AbstractScene extends Scene with ScalaFxView {

	content = new BorderPane {

		top = new TitleView

		center =
			new VBox {
				spacing = 25
				padding = Insets(50)
				alignment = Pos.CENTER

				content = contentCenter
			}

	}

	def contentCenter: List[Node]

}