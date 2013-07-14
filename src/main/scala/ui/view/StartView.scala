package ui.view
import scalafx.scene.layout._
import scalafx.scene.effect._
import scalafx.scene.paint._
import scalafx.scene.control.Label
import scalafx.scene.control.TextField
import scalafx.scene.control.Button
import scalafx.geometry.Pos
import javafx.event.ActionEvent
import ui.presenter.StartPresenter

class StartView(presenter: StartPresenter) extends AbstractScene {

	private lazy val nameField = new TextField {
		text = "Spieler 1"
	}

	show(List(
		new HBox {
			alignment = Pos.CENTER
			spacing = 20

			content = List(
				new Label {
					text = "Name"
				},
				nameField
			)
		},
		new HBox {
			alignment = Pos.CENTER
			spacing = 20

			content = List(
				new Button {
					text = "Spiel erstellen"
					onAction = (event: ActionEvent) => presenter.createGame
				},
				new Button {
					text = "Spiel beitreten"
					onAction = (event: ActionEvent) => presenter.joinGame
				},
				new Button {
					text = "Direkt mit IP verbinden"
					onAction = (event: ActionEvent) => presenter.connectIp
				}
			)
		}
	))

	def playerName: String = nameField.text.value
}