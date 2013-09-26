package game

import model.Player

case class Game(player1: Player, player2: Player, player3: Player, player4: Player) {
	val gameStates = Map(
		player1 -> GameState,
		player2 -> GameState,
		player3 -> GameState,
		player4 -> GameState
	)
}

case class GameState {
	val figures = 1 to 4 map (_ => Figure())

	def getFigureOnStart = figures filter (_.position.isInstanceOf[Start]) head
}

case class Figure(position: Position = Start())

sealed trait Position

case class Field(field: Int) extends Position

case class Home(field: Int) extends Position {
	require(field >= 0 && field <= 4)
}

case class Start extends Position