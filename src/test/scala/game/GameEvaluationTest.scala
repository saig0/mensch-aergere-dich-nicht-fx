package game

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest._
import org.scalatest.matchers.Matchers
import org.scalatest.matchers.ShouldMatchers._
import model._

@RunWith(classOf[JUnitRunner])
class GameEvaluationTest extends FlatSpec with Matchers {

	val players = List(Player("Player 1"), Player("Player 2"), Player("Player 3"), Player("Player 4"))

	def newGame = new Game(players)

	def fieldBeforeHome(game: Game, player: Player, fields: Int) =
		Field(game.startPosition(player) - fields + 1)

	"The best move" should "move figure to home" in {
		val game = newGame
		val dice = 3
		val player1 = players(0)
		val figuresOfPlayer1 = game.gameStates(player1).figures
		val player2 = players(1)
		val figuresOfPlayer2 = game.gameStates(player2).figures

		game.moveFigure(player1, figuresOfPlayer1(0), fieldBeforeHome(game, player1, 10))
		game.moveFigure(player1, figuresOfPlayer1(1), fieldBeforeHome(game, player1, dice))
		game.moveFigure(player2, figuresOfPlayer2(0), fieldBeforeHome(game, player1, 10))

		GameEvaluation.getBestMove(game, player1, dice) should be(figuresOfPlayer1(1))
	}

	it should "beat an other figure" in {
		val game = newGame
		val dice = 3
		val player1 = players(0)
		val figuresOfPlayer1 = game.gameStates(player1).figures
		val player2 = players(1)
		val figuresOfPlayer2 = game.gameStates(player2).figures

		game.moveFigure(player1, figuresOfPlayer1(0), fieldBeforeHome(game, player1, dice - 1))
		game.moveFigure(player1, figuresOfPlayer1(1), fieldBeforeHome(game, player1, 10))
		game.moveFigure(player2, figuresOfPlayer2(0), fieldBeforeHome(game, player1, 10))

		GameEvaluation.getBestMove(game, player1, dice) should be(figuresOfPlayer1(1))
	}

	it should "move figure from start" in {
		val game = newGame
		val dice = 3
		val player = players(0)
		val figures = game.gameStates(player).figures

		game.moveFigure(player, figures(0), fieldBeforeHome(game, player, 10))
		game.moveFigure(player, figures(1), fieldBeforeHome(game, player, 0))

		GameEvaluation.getBestMove(game, player, dice) should be(figures(1))
	}

	it should "move figure into game" in {
		val game = newGame
		val dice = 6
		val player = players(0)
		val figures = game.gameStates(player).figures

		game.moveFigure(player, figures(0), fieldBeforeHome(game, player, 10))

		GameEvaluation.getBestMove(game, player, dice) should be(figures(1))
	}

	it should "move nearest figure to home" in {
		val game = newGame
		val dice = 3
		val player = players(0)
		val figures = game.gameStates(player).figures

		game.moveFigure(player, figures(0), fieldBeforeHome(game, player, 10))
		game.moveFigure(player, figures(1), fieldBeforeHome(game, player, 5))

		GameEvaluation.getBestMove(game, player, dice) should be(figures(1))
	}
}