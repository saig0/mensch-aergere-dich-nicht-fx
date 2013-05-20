package ui

import model.Player

class StartPresenter extends Presenter[StartView] {

	val view = new StartView(StartPresenter.this)

	def createGame {
		val presenter = new GameCreationPresenter
		Main.loadPresenter(presenter)
		val player = Player(view.playerName)
		presenter.show(player)
	}

	def joinGame {}
}