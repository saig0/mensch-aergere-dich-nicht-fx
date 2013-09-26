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

	lazy val diceLabel = new Label {
		visible = false
	}

	lazy val dice = new Group {
		visible = false

		children = Seq(cube) ++ points.flatten

		lazy val cube = new Rectangle {
			x = -25
			y = -25
			height = 50
			width = 50
			fill = LIGHTGRAY
		}

		lazy val points = Seq(
			Seq(point(0, 0)),
			Seq(point(-10, -10), point(10, 10)),
			Seq(point(-10, -10), point(0, 0), point(10, 10)),
			Seq(point(-10, -10), point(-10, 10), point(10, -10), point(10, 10)),
			Seq(point(-10, -10), point(-10, 10), point(0, 0), point(10, -10), point(10, 10)),
			Seq(point(-10, -10), point(-10, 0), point(-10, 10), point(10, -10), point(10, 0), point(10, 10))
		)

		def point(x: Int, y: Int) = new Circle {
			centerX = x
			centerY = y
			radius = 5
			fill = WHITE
			visible = false
		}

		def number(number: Int) {
			points map (s => s map (_.visible = s.size == number))
		}
	}

	lazy val gameField = new Group {

		val gameFieldRange = 25

		val x = 0
		val y = 0

		val playerColors = Seq(BLUE, GREEN, YELLOW, RED)

		children = gameFields ++ startFields ++ homeFields

		lazy val gameFields = Set(
			1 to 3 map (i => gameField(x, y + i * gameFieldRange)),
			0 to 3 map (i => gameField(x + i * gameFieldRange, y + 4 * gameFieldRange)),
			0 to 1 map (i => gameField(x + 4 * gameFieldRange, y + 4 * gameFieldRange + i * gameFieldRange)),
			1 to 3 map (i => gameField(x + 4 * gameFieldRange - i * gameFieldRange, y + 6 * gameFieldRange)),
			0 to 3 map (i => gameField(x, y + 6 * gameFieldRange + i * gameFieldRange)),
			0 to 1 map (i => gameField(x - i * gameFieldRange, y + 10 * gameFieldRange)),
			1 to 3 map (i => gameField(x - 2 * gameFieldRange, y + 10 * gameFieldRange - i * gameFieldRange)),
			0 to 3 map (i => gameField(x - 2 * gameFieldRange - i * gameFieldRange, y + 6 * gameFieldRange)),
			0 to 1 map (i => gameField(x - 6 * gameFieldRange, y + 6 * gameFieldRange - i * gameFieldRange)),
			1 to 3 map (i => gameField(x - 6 * gameFieldRange + i * gameFieldRange, y + 4 * gameFieldRange)),
			0 to 3 map (i => gameField(x - 2 * gameFieldRange, y + 4 * gameFieldRange - i * gameFieldRange)),
			0 to 1 map (i => gameField(x - 2 * gameFieldRange + i * gameFieldRange, y))
		).flatten

		lazy val startFields = Seq(
			playerField(x, y, playerColors(0)),
			playerField(x + 4 * gameFieldRange, y + 6 * gameFieldRange, playerColors(1)),
			playerField(x - 2 * gameFieldRange, y + 10 * gameFieldRange, playerColors(2)),
			playerField(x - 6 * gameFieldRange, y + 4 * gameFieldRange, playerColors(3))
		)

		lazy val homeFields = Seq(
			1 to 4 map (i => homeField(x - 1 * gameFieldRange, y + i * gameFieldRange, playerColors(0))),
			1 to 4 map (i => homeField(x + 4 * gameFieldRange - i * gameFieldRange, y + 5 * gameFieldRange, playerColors(1))),
			1 to 4 map (i => homeField(x - 1 * gameFieldRange, y + 10 * gameFieldRange - i * gameFieldRange, playerColors(2))),
			1 to 4 map (i => homeField(x - 6 * gameFieldRange + i * gameFieldRange, y + 5 * gameFieldRange, playerColors(3)))
		).flatten

		def gameField(x: Int, y: Int) = field(x, y)

		def playerField(x: Int, y: Int, playerColor: Color) = field(x, y, playerColor)

		def homeField(x: Int, y: Int, playerColor: Color) = field(x, y, playerColor, GRAY)

		def field(x: Int, y: Int, fillColor: Color = WHITE, strokeColor: Color = BLACK): Circle = new Circle {
			centerX = x
			centerY = y
			radius = 10
			fill = fillColor
			stroke = strokeColor
		}
	}

	show {
		List(
			players,
			activePlayer,
			diceLabel,
			dice,
			gameField
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

		dice.number(number)
		dice.visible = true

		new SequentialTransition {
			children = Seq(
				new ParallelTransition {
					node = dice
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