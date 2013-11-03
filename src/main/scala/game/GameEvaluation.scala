package game

import model.Player

object GameEvaluation {
	def getBestMove(game: Game, player: Player, dice: Int): Figure = {
		// TODO: intelligentere Auswahl der Figur
		val possibleMovements = game.gameStates(player).figures map (figure => game.nextPositions(player, figure, dice) map (movement => figure -> movement)) flatten
		val figure = possibleMovements.head._1
		figure
	}
}