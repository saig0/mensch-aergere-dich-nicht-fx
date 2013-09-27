package ui.view

import scalafx.scene.Group
import scalafx.scene.shape._
import scalafx.scene.paint.Color._
import scalafx.animation._
import scalafx.Includes._
import scala.concurrent._
import ExecutionContext.Implicits.global

class Dice {

	lazy val view = new Group {
		visible = false

		children = Seq(cube) ++ points.flatten

		lazy val cube = new Rectangle {
			x = -25
			y = -25
			height = 50
			width = 50
			fill = LIGHTGRAY
		}

	}

	lazy val points = Seq(
		Seq(point(0, 0)),
		Seq(point(-10, -10), point(10, 10)),
		Seq(point(-10, -10), point(0, 0), point(10, 10)),
		Seq(point(-10, -10), point(-10, 10), point(10, -10), point(10, 10)),
		Seq(point(-10, -10), point(-10, 10), point(0, 0), point(10, -10), point(10, 10)),
		Seq(point(-10, -10), point(-10, 0), point(-10, 10), point(10, -10), point(10, 0), point(10, 10))
	)

	private def point(x: Int, y: Int) = new Circle {
		centerX = x
		centerY = y
		radius = 5
		fill = WHITE
		visible = false
	}

	private def show(number: Int) =
		points map (s => s map (_.visible = s.size == number))

	private def showNumber(number: Int) =
		1 to 6 map (s => future {
			Thread.sleep(s * 300)
			if (s < 6) {
				show((1 + Math.random * 5).toInt)
			} else {
				show(number)
			}
		})

	private def rollAnimation =
		new ParallelTransition {
			node = view
			children = Seq(
				new FadeTransition {
					fromValue = 0
					toValue = 1
					duration = (0.5 s)
				},
				new TranslateTransition {
					duration = (2 s)
					cycleCount = 1
					fromX = -300
					toX = 300
				},
				new RotateTransition {
					duration = (2 s)
					cycleCount = 1
					byAngle = 180
				}
			)
		}

	def roll(number: Int) {
		view.visible = true
		showNumber(number)
		rollAnimation.play
	}
}