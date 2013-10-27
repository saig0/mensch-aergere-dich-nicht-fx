package ui.view

import scalafx.scene.Group
import scalafx.scene.shape._
import scalafx.scene.paint.Color
import scalafx.scene.paint.Color._
import model.Player
import game._
import scalafx.scene.Node
import javafx.scene.input.MouseEvent
import ui.view.ScalaFxView.actionDsl
import ui.presenter.GamePresenter
import scalafx.animation._
import scalafx.Includes._
import ui.view.PlayerFigure._

object PlayerFigure {
	val moveAnimationDuration = 1 s

	val moveAnimationDurationInMillis = moveAnimationDuration.toMillis.toInt
}

case class PlayerFigure(x: Int, y: Int, color: Color, player: Player, var figure: Figure, presenter: GamePresenter) {

	lazy val view = new Group {
		private def body = {
			val body = Polygon(
				x, y - 22,
				x + 9, y,
				x - 9, y)
			body fill = color
			body stroke = LIGHTGRAY
			body strokeWidth = 2
			body
		}

		private def head = new Circle {
			centerX = x
			centerY = y - 20
			radius = 8
			fill = color
			stroke = LIGHTGRAY
			strokeWidth = 2
		}

		children = Seq(
			body,
			head
		)

		onMouseEntered = (_: MouseEvent) => presenter.previewFigure(player, figure)

		onMouseExited = (_: MouseEvent) => presenter.removePreviewFigure

		onMouseClicked = (_: MouseEvent) => presenter.moveFigure(player, figure)

	}

	def move(movement: List[(Double, Double)]) {
		new SequentialTransition {
			node = view
			children = movement map { move =>
				new TranslateTransition {
					duration = moveAnimationDuration
					toX = move._1 - x
					toY = move._2 - y
				}
			}
		}.play
	}
}