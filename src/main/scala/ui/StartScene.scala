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

class StartScene extends AbstractScene {

	lazy val nameField = new TextField {
		text = "Spieler 1"
	}

	def contentCenter = List(
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
					onAction = (event: ActionEvent) => {
						val scene = new GameCreationScene(nameField.text.value)
						Main.loadSceen(scene)
					}
				},
				new Button {
					text = "Spiel beitreten"
				}
			)
		}
	)

	def onShow {}

}