package ui.view
import scalafx.scene.layout._
import scalafx.scene.effect._
import scalafx.scene.paint._
import scalafx.scene.control.Label
import scalafx.scene.control.ProgressIndicator
import ui.presenter.GamePresenter

class GameView(presenter: GamePresenter) extends AbstractScene {

	def contentCenter = List(
		new Label {
			text = "Starte Spiel"
		},
		new ProgressIndicator {
			import ui.view.AbstractScene
			progress = -1
		}
	)

}