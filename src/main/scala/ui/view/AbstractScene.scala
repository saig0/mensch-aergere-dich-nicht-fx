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
import scalafx.scene.control.Label
import scalafx.scene.control.ProgressIndicator

abstract class AbstractScene extends Scene with ScalaFxView {

	content = new BorderPane {

		top = new TitleView

		center = contentCenter
	}

	private lazy val contentCenter = new VBox {
		spacing = 25
		padding = Insets(50)
		alignment = Pos.CENTER
	}

	def show(node: Node) {
		contentCenter.content = node
	}

	def show(nodes: List[Node]) {
		contentCenter.content = nodes
	}

	def showLoading(loadingText: String) {
		show(List(
			new Label {
				text = loadingText
			},
			new ProgressIndicator {
				progress = -1
			})
		)
	}
}