package ui.view
import scalafx.scene.layout._
import scalafx.scene.effect._
import scalafx.scene.paint._
import scalafx.scene.control.Label
import scalafx.scene.control.ProgressIndicator
import ui.presenter.GamePresenter
import model.Player

class GameView(presenter: GamePresenter) extends AbstractScene {

	def waitForStart {
		show {
			List(
				new Label {
					text = "Starte Spiel"
				},
				new ProgressIndicator {
					import ui.view.AbstractScene
					progress = -1
				}
			)
		}
	}

	def start(players: List[Player]) {
		show {
			List(
				new Label {
					text = "Spiel gestartet"
				},
				new Label {
					text = "Spieler: " + players.map(_.name).mkString(",")
				}
			)
		}
	}
}