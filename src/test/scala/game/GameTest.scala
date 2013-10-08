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

	"A figure" should "start on field 1 with 1 for player 1" in {
		val game = new Game(players)
		game.nextPositions(players(0), Figure(Start(0)), 1) should be(Some(List(Field(1))))
	}

	it should "start on field 11 with 1 for player 2" in {
		val game = new Game(players)
		game.nextPositions(players(1), Figure(Start(0)), 1) should be(Some(List(Field(11))))
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

	it should " move to field with a figure of other player" in {
		val game = new Game(players)
		val player1 = players(0)
		val player2 = players(1)
		val figure1 = game.gameStates(player1).figures(0)
		game.moveFigure(player1, figure1, Field(3))
		game.nextPositions(player2, Figure(Field(1)), 2) should be(Some(List(Field(2), Field(3))))
	}
}