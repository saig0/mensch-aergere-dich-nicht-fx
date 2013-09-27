package ui.view
import scalafx.scene.layout._
import scalafx.scene.effect._
import scalafx.scene.paint._
import scalafx.scene.control.Label
import scalafx.scene.control.ProgressIndicator
import ui.presenter.GamePresenter
import model.Player
import scalafx.scene.shape.Rectangle
import scalafx.animation.Timeline
import scalafx.Includes._
import scalafx.animation.Timeline._
import scalafx.scene.paint.Color._
import scalafx.animation.TranslateTransition
import scalafx.animation.SequentialTransition
import scalafx.animation.RotateTransition
import scalafx.animation.ParallelTransition
import scalafx.animation.FadeTransition
import scalafx.scene.text.Text
import scalafx.event.ActionEvent
import scalafx.scene.Group
import scalafx.scene.shape.Circle

class GameView(presenter: GamePresenter) extends AbstractScene {

	lazy val players = new Label {}

	lazy val activePlayer = new Label {}

	lazy val dice = new Dice
	lazy val gameField = new GameField

	show {
		List(
			players,
			activePlayer,
			dice.view,
			gameField.view
		)
	}

	def players(p: List[Player]) {
		players.text = "Spieler: " + p.map(_.name).mkString(", ")
	}

	def yourTurn(player: Player) {
		activePlayer.text = "Dein Zug " + player.name
	}

	def newTurn(player: Player) {
		activePlayer.text = player.name + " ist am Zug."
	}

	def dice(number: Int) {
		dice.roll(number)
	}
}