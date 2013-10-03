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
import scalafx.geometry.Pos._
import scalafx.scene.text.Font
import scalafx.scene.text.FontWeight
import scalafx.scene.shape.Polygon
import game.Figure
import game.Game
import game.Position

class GameView(presenter: GamePresenter) extends AbstractScene {

	lazy val players = 0 to 3 map (_ => new Label)

	lazy val activePlayer = new Label {}

	lazy val dice = new Dice
	lazy val gameField = new GameField(presenter)

	lazy val gameFieldWithPlayerNames = new VBox {
		alignment = CENTER
		spacing = 10
		content = Seq(
			players(0),
			new HBox {
				alignment = CENTER
				spacing = 10
				content = Seq(
					players(3),
					gameField.view,
					players(1)
				)
			},
			players(2)
		)
	}

	show {
		List(
			activePlayer,
			dice.view,
			gameFieldWithPlayerNames
		)
	}

	def players(player: List[Player]) {
		0 to 3 map (i => players(i).text = player(i).name)
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

	def showGame(game: Game) {
		gameField.showGame(game)
	}

	def previewPositions(positions: List[Position]) {
		gameField.previewPositions(positions)
	}

	def moveFigure(player: Player, figure: Figure, movement: List[Position]) {
		gameField.moveFigure(player, figure, movement)
	}
}