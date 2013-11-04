package game

import model.Player

object GameEvaluation {

	private val evaluationWin = 90
	private val evaluationHomeField = 80
	private val evaluationBeatFigure = 70
	private val evaluationMoveFigureFromStart = 60
	private val evaluationMoveFigureIntoGame = 50

	def getBestMove(game: Game, player: Player, dice: Int): Figure = {
		val possibleMovements = game.gameStates(player).figures map (figure => game.nextPositions(player, figure, dice) map (movement => figure -> movement.last)) flatten
		val (figure, rating) = possibleMovements map { case (figure, movement) => figure -> evaluateMovement(copyGame(game), player, figure, movement, dice) } maxBy (_._2)
		figure
	}

	private def copyGame(game: Game): Game = {
		val newGame = game.copy()
		game.gameStates map {
			case (player, gameState) => 0 to 3 map { i =>
				val figure = newGame.gameStates(player).figures(i)
				val pos = gameState.figures(i).position
				newGame.moveFigure(player, figure, pos)
			}
		}
		newGame
	}

	private def evaluateMovement(game: Game, player: Player, figure: Figure, movement: Position, dice: Int): Int = {
		game.moveFigure(player, figure, movement) map {
			_ match {
				case Win(_) => evaluationWin
				case BeatFigure(_, _) => evaluationBeatFigure
			}
		} getOrElse {
			movement match {
				case Home(_) => evaluationHomeField
				case Field(pos) if (pos == startPosition(game, player) + dice) => evaluationMoveFigureFromStart
				case Field(pos) if (pos == startPosition(game, player)) => evaluationMoveFigureIntoGame
				case Field(pos) => pos
			}
		}
	}

	private def startPosition(game: Game, player: Player) =
		(game.startPosition(player) % Game.gameFieldCount) + 1

}