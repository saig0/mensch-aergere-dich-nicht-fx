package game

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest._
import org.scalatest.matchers.Matchers
import org.scalatest.matchers.ShouldMatchers._
import model._

@RunWith(classOf[JUnitRunner])
class GameTest extends FlatSpec with Matchers {

	val players = List(Player("Player 1"), Player("Player 2"), Player("Player 3"), Player("Player 4"))

	"A figure" should "start on field 1 with 6 for player 1" in {
		val game = new Game(players)
		game.nextPositions(players(0), Figure(Start(0)), 6) should be(Some(List(Field(1))))
	}

	it should "start on field 11 with 6 for player 2" in {
		val game = new Game(players)
		game.nextPositions(players(1), Figure(Start(0)), 6) should be(Some(List(Field(11))))
	}

	it should "not start with 1" in {
		val game = new Game(players)
		game.nextPositions(players(0), Figure(Start(0)), 1) should be(None)
	}

	it should "not start with 2" in {
		val game = new Game(players)
		game.nextPositions(players(0), Figure(Start(0)), 2) should be(None)
	}

	it should "move 2 fields with 2 for player 1" in {
		val game = new Game(players)
		game.nextPositions(players(0), Figure(Field(1)), 2) should be(Some(List(Field(2), Field(3))))
	}

	it should "move 2 fields with 2 for player 2" in {
		val game = new Game(players)
		game.nextPositions(players(1), Figure(Field(11)), 2) should be(Some(List(Field(12), Field(13))))
	}

	it should "go to home with 1 for player 1" in {
		val game = new Game(players)
		game.nextPositions(players(0), Figure(Field(40)), 1) should be(Some(List(Home(1))))
	}

	it should "go to home with 1 for player 2" in {
		val game = new Game(players)
		game.nextPositions(players(1), Figure(Field(10)), 1) should be(Some(List(Home(1))))
	}

	it should "go to home with 4 for player 1" in {
		val game = new Game(players)
		game.nextPositions(players(0), Figure(Field(40)), 4) should be(Some(List(Home(1), Home(2), Home(3), Home(4))))
	}

	it should "not go to home with 5" in {
		val game = new Game(players)
		game.nextPositions(players(0), Figure(Field(40)), 5) should be(None)
	}

	it should "not go to home with 6" in {
		val game = new Game(players)
		game.nextPositions(players(0), Figure(Field(40)), 6) should be(None)
	}

	it should "not move to field with a figure of same player" in {
		val game = new Game(players)
		val player1 = players(0)
		val figure1 = game.gameStates(player1).figures(0)
		game.moveFigure(player1, figure1, Field(3))
		game.nextPositions(player1, Figure(Field(1)), 2) should be(None)
	}

	it should "move to field with a figure of other player" in {
		val game = new Game(players)
		val player1 = players(0)
		val player2 = players(1)
		val figure1 = game.gameStates(player1).figures(0)
		game.moveFigure(player1, figure1, Field(3))
		game.nextPositions(player2, Figure(Field(1)), 2) should be(Some(List(Field(2), Field(3))))
	}

	"A move of a figure" should "change the game state" in {
		val game = new Game(players)
		val player = players(0)
		val figure = game.gameStates(player).figures(0)
		figure.position should be(Start(0))

		val newPosition = Field(1)
		game.moveFigure(player, figure, newPosition)
		figure.position should be(newPosition)
	}

	it should "not beat a figure of other player" in {
		val game = new Game(players)
		val player1 = players(0)
		val player2 = players(1)
		val figure1 = game.gameStates(player1).figures(0)
		game.moveFigure(player1, figure1, Field(3))
		val figure2 = game.gameStates(player2).figures(0)
		game.moveFigure(player2, figure2, Field(2)) should be(None)
	}

	it should "not beat a figure of other player on start" in {
		val game = new Game(players)
		val player1 = players(0)
		val player2 = players(1)
		val figure1 = game.gameStates(player1).figures(0)
		game.moveFigure(player1, figure1, Start(0))
		val figure2 = game.gameStates(player2).figures(0)
		game.moveFigure(player2, figure2, Start(0)) should be(None)
	}

	it should "beat a figure of other player" in {
		val game = new Game(players)
		val player1 = players(0)
		val player2 = players(1)
		val figure1 = game.gameStates(player1).figures(0)
		game.moveFigure(player1, figure1, Field(3))
		val figure2 = game.gameStates(player2).figures(0)
		game.moveFigure(player2, figure2, Field(3)) should be(Some(BeatFigure(player1, figure1)))
	}

	it should "not win the game with missing figures" in {
		val game = new Game(players)
		val player1 = players(0)
		val figures = game.gameStates(player1).figures

		game.moveFigure(player1, figures(0), Home(4))
		game.moveFigure(player1, figures(1), Home(3))
		game.moveFigure(player1, figures(2), Home(2)) should be(None)
	}

	it should "win the game" in {
		val game = new Game(players)
		val player1 = players(0)
		val figures = game.gameStates(player1).figures

		game.moveFigure(player1, figures(0), Home(4))
		game.moveFigure(player1, figures(1), Home(3))
		game.moveFigure(player1, figures(2), Home(2))
		game.moveFigure(player1, figures(3), Home(1)) should be(Some(Win(player1)))
	}

	"A player" should "end turn after move a figure on field with 1" in {
		val game = new Game(players)
		val player = players(0)
		val figure = game.gameStates(player).figures(0)

		game.moveFigure(player, figure, Field(2))
		game.nextAction(player, 1) should be(EndTurn())
	}

	it should "roll dice again after move a figure on field with 6" in {
		val game = new Game(players)
		val player = players(0)
		val figure = game.gameStates(player).figures(0)

		game.moveFigure(player, figure, Field(6))
		game.nextAction(player, 6) should be(RollDiceAgain(player))
	}

	it should "roll dice again while trying to move figure from start for 3 times" in {
		val game = new Game(players)
		val player = players(0)
		val figure = game.gameStates(player).figures(0)

		game.nextAction(player, 1) should be(RollDiceAgain(player))
		game.nextAction(player, 2) should be(RollDiceAgain(player))
		game.nextAction(player, 3) should be(RollDiceAgain(player))
	}

	it should "not roll dice again while trying to move figure from start for more than 3 times" in {
		val game = new Game(players)
		val player = players(0)
		val figure = game.gameStates(player).figures(0)

		game.nextAction(player, 1) should be(RollDiceAgain(player))
		game.nextAction(player, 2) should be(RollDiceAgain(player))
		game.nextAction(player, 3) should be(RollDiceAgain(player))
		game.nextAction(player, 4) should be(EndTurn())
	}
}