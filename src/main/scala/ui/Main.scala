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
	}

	loadSceen(new StartScene)

	def loadSceen[S <: AbstractScene](scene: S) = {
		scene.stylesheets += loadCss
		stage.scene = scene
		scene.onShow
	}

	private lazy val loadCss =
		getClass().getResource("/default.css").toExternalForm()
}