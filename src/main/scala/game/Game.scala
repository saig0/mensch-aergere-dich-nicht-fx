package game

import model.Player

case class Game(players: List[Player]) {
	val gameStates = players map (player => player -> GameState())

	def nextPositions(player: Player, figure: Figure, dice: Int): List[Position] =
		1 to dice map (nextPosition(player, figure, _)) toList

	private def nextPosition(player: Player, figure: Figure, dice: Int): Position = {
		// Implementieren
		figure.position match {
			case Start(_) => Field(dice)
			case Field(pos) => Field(pos + dice)
			case Home(pos) => Home(pos)
		}
	}
}

case class GameState {
	val figures = 0 to 3 map (i => Figure(Start(i)))

	def getFigureOnStart = figures filter (_.position.isInstanceOf[Start]) headOption
}

case class Figure(position: Position)

sealed trait Position

case class Field(field: Int) extends Position

case class Home(field: Int) extends Position {
	require(field >= 0 && field <= 4)
}

case class Start(field: Int) extends Position