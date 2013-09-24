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

class GameView(presenter: GamePresenter) extends AbstractScene {

	lazy val players = new Label {}

	lazy val activePlayer = new Label {}

	lazy val diceLabel = new Label {
		visible = false
	}

	lazy val dice = new Rectangle {
		x = -25
		y = -25
		height = 50
		width = 50
		fill = LIGHTGRAY
	}

	show {
		List(
			players,
			activePlayer,
			diceLabel,
			dice
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
		diceLabel.text = "Es wurde eine " + number + " gewürfelt."

		new SequentialTransition {
			children = Seq(
				new ParallelTransition {
					node = dice
					children = Seq(
						new TranslateTransition {
							duration = (2 s)
							cycleCount = 1
							fromX = -100
							toX = 500
						},
						new RotateTransition {
							duration = (2 s)
							cycleCount = 1
							byAngle = 180
						}
					)
					onFinished = (event: ActionEvent) => {
						diceLabel.visible = true
					}
				},
				new FadeTransition {
					node = diceLabel
					fromValue = 0
					toValue = 1
					duration = (2 s)
				}
			)
		}.play
	}
}