package ui.view
import scalafx.scene.layout._
import scalafx.scene.effect._
import scalafx.scene.paint._
import scalafx.scene.control.Label
import scalafx.scene.control.TextField
import scalafx.scene.control.Button
import scalafx.geometry.Pos
import dispatch._
import dispatch.Defaults._
import javafx.event.ActionEvent
import ui.presenter.ConnectGamePresenter

class ConnectGameView(presenter: ConnectGamePresenter) extends AbstractScene {

	private lazy val ipField = new TextField {
	}

	private lazy val connectButton = new Button {
		text = "verbinden"
		onAction = (event: ActionEvent) => presenter.joinGame
	}

	def ip: String = ipField.text.value

	def contentCenter = List(
		new HBox {
			alignment = Pos.CENTER
			spacing = 20
			content = List(
				new Label {
					text = "IP des Spiel-Servers"
				},
				ipField,
				connectButton,
				new Button {
					text = "abbrechen"
					onAction = (event: ActionEvent) => presenter.abort
				}
			)
		}
	)
}