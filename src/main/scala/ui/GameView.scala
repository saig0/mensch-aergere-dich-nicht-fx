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
import scalafx.scene.control.ProgressIndicator
class GameView(presenter: GamePresenter) extends AbstractScene {

	def contentCenter = List(
		new Label {
			text = "Starte Spiel"
		},
		new ProgressIndicator {
			progress = -1
		}
	)

}