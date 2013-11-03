package game

import model.Player

object GameEvaluation {
	def getBestMove(game: Game, player: Player, dice: Int): Figure = {
		val possibleMovements = game.gameStates(player).figures map (figure => game.nextPositions(player, figure, dice) map (movement => figure -> movement.last)) flatten
		val (figure, rating) = possibleMovements map { case (figure, movement) => figure -> evaluateMovement(game, player, figure, movement, dice) } maxBy (_._2)
		figure
	}

	private val evaluationWin = 90
	private val evaluationHomeField = 80
	private val evaluationBeatFigure = 70
	private val evaluationMoveFigureFromStart = 60
	private val evaluationMoveFigureIntoGame = 50

	private def startPosition(game: Game, player: Player) =
		(game.startPosition(player) % Game.gameFieldCount) + 1

	private def evaluateMovement(game: Game, player: Player, figure: Figure, movement: Position, dice: Int): Int = {
		// val game = g.copy()
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
}