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
		text = "127.0.0.1"
	}

	private lazy val connectButton = new Button {
		text = "verbinden"
		onAction = (event: ActionEvent) => presenter.joinGame
	}

	private lazy val messageField = new Label

	def ip: String = ipField.text.value

	def showView = {
		show {
			List(
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
				},
				messageField
			)
		}
		messageField text = ""
	}

	def failedToConnect(cause: String) {
		showView
		messageField text = "Es konnte keine Verbindung zum Server " + ip + " hergestellt werden."
	}
}