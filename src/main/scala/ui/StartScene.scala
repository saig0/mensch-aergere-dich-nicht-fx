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

class StartScene extends Scene {

	content = new BorderPane {

		top = new HBox {
			padding = Insets(20)

			content = List(
				new Text {
					text = "Mensch Ärgere Dich Nicht"
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

				content = List(
					new HBox {
						alignment = Pos.CENTER
						spacing = 20

						content = List(
							new Label {
								text = "Name"
							},
							new TextField {

							}
						)
					},
					new HBox {
						alignment = Pos.CENTER
						spacing = 20

						content = List(
							new Button {
								text = "Spiel erstellen"

							},
							new Button {
								text = "Spiel beitreten"
							}
						)
					}
				)
			}

	}

}