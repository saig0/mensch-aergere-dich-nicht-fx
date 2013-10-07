package game

import model.Player

object Game {
	val gameFieldCount = 40
}

import Game._

case class Game(players: List[Player]) {
	val gameStates = players map (player => player -> GameState()) toMap

	private def startPositions(player: Player) = {
		(players indexOf player) match {
			case 0 => gameFieldCount
			case i => i * 10
		}
	}

	def nextPositions(player: Player, figure: Figure, dice: Int): List[Position] =
		1 to dice map (nextPosition(player, figure, _)) toList

	// TODO: auf Kollisionen mit anderen Figuren pr�fen	
	private def nextPosition(player: Player, figure: Figure, dice: Int): Position = {
		figure.position match {
			case Start(_) => Field(startPositions(player) % gameFieldCount + dice) // nur mit einer 6 starten
			case Field(pos) if (pos + dice <= startPositions(player)) => {
				if (pos + dice == gameFieldCount) Field(gameFieldCount)
				else Field((pos + dice) % gameFieldCount)
			}
			case Field(pos) if (pos + dice > startPositions(player) && pos + dice - startPositions(player) <= 4) => Home(pos + dice - startPositions(player))
			case Home(pos) if (pos + dice <= 4) => Home(pos + dice)
			case p => p
		}
	}

	def moveFigure(player: Player, figure: Figure, newPosition: Position) {
		gameStates(player).figures filter (_ == figure) map (_.position = newPosition)
	}
}

case class GameState {
	val figures = 0 to 3 map (i => Figure(Start(i)))

	def getFigureOnStart = figures filter (_.position.isInstanceOf[Start]) headOption
}

case class Figure(var position: Position)

sealed trait Position

case class Field(field: Int) extends Position {
	require(field >= 1 && field <= Game.gameFieldCount)
}

case class Home(field: Int) extends Position {
	require(field >= 1 && field <= 4)
}

case class Start(field: Int) extends Position {
	require(field >= 0 && field <= 3)
}