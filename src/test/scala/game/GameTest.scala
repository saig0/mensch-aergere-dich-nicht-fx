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
		game.nextPositions(players(0), Figure(Start(0)), 1) should be(List(Field(1)))
	}

	it should "start on field 11 with 1 for player 2" in {
		val game = new Game(players)
		game.nextPositions(players(1), Figure(Start(0)), 1) should be(List(Field(11)))
	}

	it should "move 2 fields with 2" in {
		val game = new Game(players)
		game.nextPositions(players(0), Figure(Field(1)), 2) should be(List(Field(2), Field(3)))
	}

	it should "go to home with 1 for player 1" in {
		val game = new Game(players)
		game.nextPositions(players(0), Figure(Field(40)), 1) should be(List(Home(1)))
	}

	it should "go to home with 1 for player 2" in {
		val game = new Game(players)
		game.nextPositions(players(1), Figure(Field(10)), 1) should be(List(Home(1)))
	}

	it should "go to home with 4 for player 1" in {
		val game = new Game(players)
		game.nextPositions(players(0), Figure(Field(40)), 4) should be(List(Home(1), Home(2), Home(3), Home(4)))
	}
}