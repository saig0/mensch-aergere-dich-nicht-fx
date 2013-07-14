package ui

class GamePresenter extends Presenter[GameView] {

	lazy val view = new GameView(this)

	val events = List(GoToGame(Nil))

	def receive = {
		case GoToGame(players) => {
			createView
		}
	}
}