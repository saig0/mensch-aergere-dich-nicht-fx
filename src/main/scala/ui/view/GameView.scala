package ui.view
import scalafx.scene.layout._
import scalafx.scene.effect._
import scalafx.scene.paint._
import scalafx.scene.control.Label
import scalafx.scene.control.ProgressIndicator
import ui.presenter.GamePresenter
import model.Player

class GameView(presenter: GamePresenter) extends AbstractScene {

	def yourTurn(player: Player) {
		show {
			new Label {
				text = "Dein Zug " + player.name
			}
		}
	}

	def newTurn(player: Player) {
		show {
			new Label {
				text = player.name + " ist am Zug."
			}
		}
	}
}