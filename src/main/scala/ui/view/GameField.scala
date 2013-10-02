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

class GameField(presenter: GamePresenter) {

	val gameFieldRange = 35

	val playerColors = Seq(BLUE, GREEN, YELLOW, RED)

	lazy val gameFields = Seq(
		Seq(playerField(0, 0, playerColors(0))),
		1 to 3 map (i => gameField(0, i * gameFieldRange)),
		0 to 3 map (i => gameField(i * gameFieldRange, 4 * gameFieldRange)),
		0 to 1 map (i => gameField(4 * gameFieldRange, 4 * gameFieldRange + i * gameFieldRange)),
		Seq(playerField(4 * gameFieldRange, 6 * gameFieldRange, playerColors(1))),
		1 to 3 map (i => gameField(4 * gameFieldRange - i * gameFieldRange, 6 * gameFieldRange)),
		0 to 3 map (i => gameField(0, 6 * gameFieldRange + i * gameFieldRange)),
		0 to 1 map (i => gameField(-i * gameFieldRange, 10 * gameFieldRange)),
		Seq(playerField(-2 * gameFieldRange, 10 * gameFieldRange, playerColors(2))),
		1 to 3 map (i => gameField(-2 * gameFieldRange, 10 * gameFieldRange - i * gameFieldRange)),
		0 to 3 map (i => gameField(-2 * gameFieldRange - i * gameFieldRange, 6 * gameFieldRange)),
		0 to 1 map (i => gameField(-6 * gameFieldRange, 6 * gameFieldRange - i * gameFieldRange)),
		Seq(playerField(-6 * gameFieldRange, 4 * gameFieldRange, playerColors(3))),
		1 to 3 map (i => gameField(-6 * gameFieldRange + i * gameFieldRange, 4 * gameFieldRange)),
		0 to 3 map (i => gameField(-2 * gameFieldRange, 4 * gameFieldRange - i * gameFieldRange)),
		0 to 1 map (i => gameField(-2 * gameFieldRange + i * gameFieldRange, 0))
	).flatten

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
		children = gameFields ++ homeFields
	}

	private def figure(x: Int, y: Int, color: Color) = new Group {

		def body = {
			val body = Polygon(
				x, y - 22,
				x + 9, y,
				x - 9, y)
			body fill = color
			body stroke = LIGHTGRAY
			body strokeWidth = 2
			body
		}

		def head = new Circle {
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
	}

	var figures = Map[(Player, Figure), Node]()

	val playerStartPool = Seq(
		(3, 1),
		(3, 9),
		(-6, 9),
		(-6, 1)
	)

	def showGame(game: Game) {
		0 to 3 map { p =>
			game.gameStates(p) match {
				case (player, gameState) =>
					gameState.figures map { f =>
						f.position match {
							case Start(position) => {
								val playerFigure = figure((playerStartPool(p)._1 + (position % 2)) * gameFieldRange, (playerStartPool(p)._2 + (position / 2)) * gameFieldRange, playerColors(p))
								playerFigure onMouseEntered = (_: MouseEvent) => presenter.previewFigure(player, f)
								playerFigure onMouseExited = {
									(_: MouseEvent) =>
										previewFields foreach (_.stroke = BLACK)
										previewFields = Nil
								}
								figures += (player -> f) -> playerFigure
							}
							case Field(position) =>
							case Home(position) =>
						}
					}
			}
		}

		figures map { case (_, figure) => view.children.add(figure) }
	}

	var previewFields = List[Circle]()

	private def previewField(field: Circle) {
		previewFields ::= field
		field stroke = RED
	}

	def previewPositions(positions: List[Position]) {
		positions map previewPosition
	}

	private def previewPosition(position: Position) {
		position match {
			case Start(pos) =>
			case Field(pos) => previewField(gameFields(pos - 1))
			case Home(pos) =>
		}
	}
}