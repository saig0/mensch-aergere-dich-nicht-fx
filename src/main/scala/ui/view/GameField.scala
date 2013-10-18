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

	var homeFields = Map[Player, Seq[Circle]]()

	private def homeFields(players: List[Player]) = Map(
		players(0) -> (1 to 4).map(i => homeField(-1 * gameFieldRange, i * gameFieldRange, playerColors(0))),
		players(1) -> (1 to 4).map(i => homeField(4 * gameFieldRange - i * gameFieldRange, 5 * gameFieldRange, playerColors(1))),
		players(2) -> (1 to 4).map(i => homeField(-1 * gameFieldRange, 10 * gameFieldRange - i * gameFieldRange, playerColors(2))),
		players(3) -> (1 to 4).map(i => homeField(-6 * gameFieldRange + i * gameFieldRange, 5 * gameFieldRange, playerColors(3)))
	)

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
		children = gameFields
	}

	def removePreviewFigure {
		previewFields foreach (_.stroke = BLACK)
		previewFields = Nil
	}

	val playerStartPool = Seq(
		(3, 1),
		(3, 9),
		(-6, 9),
		(-6, 1)
	)

	private def playerFigureCoordinates(p: Int, position: Int) =
		((playerStartPool(p)._1 + (position % 2)) * gameFieldRange,
			(playerStartPool(p)._2 + (position / 2)) * gameFieldRange)

	var figures = Set[PlayerFigure]()

	def showGame(game: Game) {
		homeFields = homeFields(game.gameStates.toList.map(_._1))
		homeFields.map(_._2).flatten.foreach(homeField => view.children.add(homeField))

		0 to 3 map { p =>
			game.gameStates.toList(p) match {
				case (player, gameState) =>
					gameState.figures map { f =>
						f.position match {
							case Start(position) => {
								val c = playerFigureCoordinates(p, position)
								val playerFigure = PlayerFigure(c._1, c._2, playerColors(p), player, f, presenter)
								figures += playerFigure
							}
						}
					}
			}
		}
		figures map { figure => view.children.add(figure.view) }
	}

	var previewFields = List[Circle]()

	def previewPositions(player: Player, positions: List[Position]) {
		val fields = positions map (pos => fieldOfPosition(player, pos))
		fields map (_.stroke = RED)
		previewFields = fields
	}

	private def fieldOfPosition(player: Player, position: Position): Circle = {
		position match {
			case Field(pos) => gameFields(pos - 1)
			case Home(pos) => homeFields(player)(pos - 1)
		}
	}

	private def coordinateOfPosition(player: Player, position: Position, playerFigure: PlayerFigure): (Double, Double) = {
		position match {
			case Start(_) => (playerFigure.x, playerFigure.y) // TODO: üdber Pos bestimmen
			case pos => {
				val field = fieldOfPosition(player, position)
				(field.centerX.toDouble, field.centerY.toDouble)
			}
		}
	}

	def moveFigure(player: Player, figure: Figure, movement: List[Position]) {
		val playerFigure = figures filter (f => f.player == player && f.figure == figure) head
		val moves = for {
			pos <- movement
		} yield coordinateOfPosition(player, pos, playerFigure)

		playerFigure.move(moves)
		playerFigure.figure.position = movement.last
	}
}