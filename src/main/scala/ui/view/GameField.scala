package ui.view

import scalafx.scene.Group
import scalafx.scene.shape._
import scalafx.scene.paint.Color
import scalafx.scene.paint.Color._

class GameField {

	val gameFieldRange = 35

	val playerColors = Seq(BLUE, GREEN, YELLOW, RED)

	lazy val gameFields = Set(
		1 to 3 map (i => gameField(0, i * gameFieldRange)),
		0 to 3 map (i => gameField(i * gameFieldRange, 4 * gameFieldRange)),
		0 to 1 map (i => gameField(4 * gameFieldRange, 4 * gameFieldRange + i * gameFieldRange)),
		1 to 3 map (i => gameField(4 * gameFieldRange - i * gameFieldRange, 6 * gameFieldRange)),
		0 to 3 map (i => gameField(0, 6 * gameFieldRange + i * gameFieldRange)),
		0 to 1 map (i => gameField(-i * gameFieldRange, 10 * gameFieldRange)),
		1 to 3 map (i => gameField(-2 * gameFieldRange, 10 * gameFieldRange - i * gameFieldRange)),
		0 to 3 map (i => gameField(-2 * gameFieldRange - i * gameFieldRange, 6 * gameFieldRange)),
		0 to 1 map (i => gameField(-6 * gameFieldRange, 6 * gameFieldRange - i * gameFieldRange)),
		1 to 3 map (i => gameField(-6 * gameFieldRange + i * gameFieldRange, 4 * gameFieldRange)),
		0 to 3 map (i => gameField(-2 * gameFieldRange, 4 * gameFieldRange - i * gameFieldRange)),
		0 to 1 map (i => gameField(-2 * gameFieldRange + i * gameFieldRange, 0))
	).flatten

	lazy val startFields = Seq(
		playerField(0, 0, playerColors(0)),
		playerField(4 * gameFieldRange, 6 * gameFieldRange, playerColors(1)),
		playerField(-2 * gameFieldRange, 10 * gameFieldRange, playerColors(2)),
		playerField(-6 * gameFieldRange, 4 * gameFieldRange, playerColors(3))
	)

	lazy val homeFields = Seq(
		1 to 4 map (i => homeField(-1 * gameFieldRange, i * gameFieldRange, playerColors(0))),
		1 to 4 map (i => homeField(4 * gameFieldRange - i * gameFieldRange, 5 * gameFieldRange, playerColors(1))),
		1 to 4 map (i => homeField(-1 * gameFieldRange, 10 * gameFieldRange - i * gameFieldRange, playerColors(2))),
		1 to 4 map (i => homeField(-6 * gameFieldRange + i * gameFieldRange, 5 * gameFieldRange, playerColors(3)))
	).flatten

	private def gameField(x: Int, y: Int) = field(x, y)

	private def playerField(x: Int, y: Int, playerColor: Color) = field(x, y, playerColor)

	private def homeField(x: Int, y: Int, playerColor: Color) = field(x, y, playerColor, GRAY)

	private def field(x: Int, y: Int, fillColor: Color = WHITE, strokeColor: Color = BLACK): Circle = new Circle {
		centerX = x
		centerY = y
		radius = 15
		fill = fillColor
		stroke = strokeColor
	}

	lazy val view = new Group {
		children = gameFields ++ startFields ++ homeFields
	}
}