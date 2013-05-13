package ui

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.stage.Stage
import scalafx.scene.layout.HBox
import scalafx.scene.text.Text
import scalafx.scene.text.Font
import scalafx.scene.paint.LinearGradient
import scalafx.scene.paint.Stop
import scalafx.scene.effect.DropShadow
import scalafx.geometry.Insets
import scalafx.scene.paint.Color
import scalafx.scene.effect.Reflection
import scalafx.scene.paint.Color.sfxColor2jfx
import scalafx.scene.paint.Stop.sfxStop2jfx
import scalafx.scene.layout.VBox

object Main extends JFXApp {

	stage = new JFXApp.PrimaryStage {
		title = "Mensch-Ärgere-Dich-Nicht-FX"

		scene = new Scene {
			stylesheets += loadCss

			content = new HBox {
				padding = Insets(20)

				content = List(
					new Text {
						text = "Mensch Ärgere Dich Nicht"
						id = "header"
						effect = new Reflection
						fill = new LinearGradient(
							endX = 0,
							stops = List(Stop(0, Color.ORANGE), Stop(1, Color.CHOCOLATE)))
					},
					new Text {
						text = " FX"
						id = "header"
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
					})
			}
		}
	}

	private lazy val loadCss =
		getClass().getResource("/default.css").toExternalForm()
}