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

	def nextPositions(player: Player, figure: Figure, dice: Int): Option[List[Position]] = {
		val movement = 1 to dice map (nextPosition(player, figure, _)) toList match {
			case Nil => None
			case m if (m exists (_ == None)) => None
			case m => Some(m flatten)
		}
		movement match {
			case Some(positions) if (!gameStates(player).figures.exists(_.position == positions.last)) => Some(positions)
			case _ => None
		}
	}

	private def nextPosition(player: Player, figure: Figure, dice: Int): Option[Position] = {
		figure.position match {
			case Start(_) => Some(Field(startPositions(player) % gameFieldCount + dice)) // nur mit einer 6 starten
			case Field(pos) if (pos > startPositions(player) || pos + dice <= startPositions(player)) => {
				if (pos + dice == gameFieldCount) Some(Field(gameFieldCount))
				else Some(Field((pos + dice) % gameFieldCount))
			}
			case Field(pos) if (pos + dice - startPositions(player) <= 4) => Some(Home(pos + dice - startPositions(player)))
			case Home(pos) if (pos + dice <= 4) => Some(Home(pos + dice))
			case _ => None
		}
	}

	def moveFigure(player: Player, figure: Figure, newPosition: Position): Option[Action] = {
		gameStates(player).figures filter (_ == figure) map (_.position = newPosition)
		players filter (_ != player) map (player =>
			gameStates(player).figures filter (_.position == newPosition && !newPosition.isInstanceOf[Start]) map (figure => (player, figure))) flatten match {
			case Nil => None
			case f :: Nil => Some(BeatFigure(f._1, f._2))
		}
	}
}

sealed trait Action

case class BeatFigure(player: Player, figure: Figure) extends Action

case class RollDiceAgain(player: Player) extends Action

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