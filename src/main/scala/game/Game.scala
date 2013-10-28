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
			case m if (m.last != None) => Some(m flatten)
			case _ => None
		}
		movement match {
			case Some(positions) if (!gameStates(player).figures.exists(_.position == positions.last)) => Some(positions)
			case _ => None
		}
	}

	private def nextPosition(player: Player, figure: Figure, dice: Int): Option[Position] = {
		figure.position match {
			case Start(_) if (dice == 6) => Some(Field(startPositions(player) % gameFieldCount + 1))
			case Field(pos) if (pos > startPositions(player) || pos + dice <= startPositions(player)) => {
				if (pos + dice == gameFieldCount) Some(Field(gameFieldCount))
				else Some(Field((pos + dice) % gameFieldCount))
			}
			case Field(pos) if (pos + dice - startPositions(player) <= 4) => Some(Home(pos + dice - startPositions(player)))
			case Home(pos) if (pos + dice <= 4) => Some(Home(pos + dice))
			case _ => None // außerhalb vom Spielbrett oder keine 6 beim Start
		}
	}

	private var history = List[(Player, Option[(Figure, Position)])]()

	def moveFigure(player: Player, figure: Figure, newPosition: Position): Option[Action] = {
		history ::= player -> Some(figure -> newPosition)

		gameStates(player).figures filter (_ == figure) map (_.position = newPosition)

		beatFigure(player, newPosition) orElse winGame(player)
	}

	private def winGame(player: Player): Option[Action] =
		gameStates(player).figures.filter(_.position.isInstanceOf[Home]) match {
			case figuresInHome if (figuresInHome.size == 4) => Some(Win(player))
			case _ => None
		}

	private def beatFigure(player: Player, newPosition: Position): Option[Action] =
		players.filter(_ != player).map(otherPlayer =>
			gameStates(otherPlayer).figures.filter(_.position == newPosition && newPosition.isInstanceOf[Field]).map(figure => (otherPlayer, figure))).flatten match {
			case (beatenPlayer, beatenFigure) :: Nil => Some(BeatFigure(beatenPlayer, beatenFigure))
			case Nil => None
		}

	def couldNotMoveFigure(player: Player) {
		history ::= player -> None
	}

	def nextAction(player: Player, dice: Int): Action = {
		if (dice == 6) {
			RollDiceAgain(player)
		} else if (allFiguresOnStartOrHome(player) && tries(player, history) < 3) {
			RollDiceAgain(player)
		} else {
			EndTurn()
		}
	}

	private def allFiguresOnStartOrHome(player: Player): Boolean = {
		val figures = gameStates(player).figures
		val figuresOnStart = figures.filter(_.position.isInstanceOf[Start]).size
		if (figuresOnStart == 4) {
			true
		} else {
			(true /: (1 to (4 - figuresOnStart) map (f => figures.exists(_.position == Home(5 - f)))))(_ && _)
		}
	}

	private def tries(player: Player, history: List[(Player, Option[(Figure, Position)])]): Int =
		history match {
			case (lastPlayer, None) :: hs if (lastPlayer == player) => 1 + tries(player, hs)
			case _ => 0
		}

	def canMoveFigure(player: Player, dice: Int): Boolean = {
		val possibleMovements = gameStates(player).figures.map(figure => nextPositions(player, figure, dice)).flatten
		!possibleMovements.isEmpty
	}
}

sealed trait Action

case class BeatFigure(player: Player, figure: Figure) extends Action

case class Win(player: Player) extends Action

case class RollDiceAgain(player: Player) extends Action

case class EndTurn extends Action

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