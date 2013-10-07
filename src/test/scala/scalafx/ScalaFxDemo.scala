package scalafx

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.beans.property.ReadOnlyBooleanProperty.sfxReadOnlyBooleanProperty2jfx
import scalafx.scene.paint.Color.sfxColor2jfx

object ScalaFxDemo extends JFXApp {
	stage = new JFXApp.PrimaryStage {
		title = "Hello World"
		width = 600
		height = 450
		scene = new Scene {
			fill = Color.LIGHTGREEN
			content = new Rectangle {
				x = 25
				y = 40
				width = 100
				height = 100
				fill <== when(hover) then Color.GREEN otherwise Color.RED
			}
		}
	}
}