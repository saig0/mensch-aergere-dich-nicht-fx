package ui

import scalafx.scene.Scene
import scalafx.scene.layout._
import scalafx.geometry.Insets
import scalafx.scene.text.Text
import scalafx.scene.effect._
import scalafx.scene.paint._
import scalafx.scene.control.Label
import scalafx.scene.control.TextField
import scalafx.scene.control.Button
import scalafx.geometry.Pos
import scalafx.scene.control.ProgressIndicator
import rest.GameServerClient
import model.Game
import scalafx.application.Platform
import dispatch._
import Defaults._
import scalafx.scene.control.TableView
import model.Player
import scalafx.scene.control.TableColumn
import javafx.event.ActionEvent

class GameCreationView(presenter: GameCreationPresenter) extends AbstractScene {

	private lazy val c = new HBox {
		alignment = Pos.CENTER
		spacing = 20
	}

	private lazy val table = new TableView[Player] {
		columns += new TableColumn[Player, String] {
			text = "Spieler"
			cellValueFactory = { _.value.name }
		}
	}

	private lazy val playersLabel = new Label {
		text = "Spieler: " + 0 + " / " + 0
	}

	private lazy val startGameButton = new Button {
		text = "Spiel starten"
		disable = true
	}

	private var data = List[Player]()

	private var game: Game = _

	def contentCenter = {
		c.content = List(
			new Label {
				text = "Erstelle Spiel auf Game-Server"
			},
			new ProgressIndicator {
				progress = -1
			}
		)
		List(c)
	}

	def showGame(game: Game) {
		this.game = game
		data = Nil

		c.content = List(
			new HBox {
				spacing = 50
				content = List(
					new VBox {
						spacing = 20
						content = List(
							new Label {
								text = "IP: " + game.address
							},
							playersLabel,
							startGameButton,
							new Button {
								text = "CPU hinzufügen"
								onAction = (event: ActionEvent) => presenter.newCpuPlayer
							},
							new Button {
								text = "abbrechen"
								onAction = (event: ActionEvent) => presenter.abort
							}
						)
					},
					table
				)
			}
		)
	}

	def joinPlayer(player: Player) {
		data ++= player :: Nil
		table.setItems(data)

		playersLabel.text = "Spieler: " + data.size + " / " + game.maxPlayers
		startGameButton.disable = data.size < game.maxPlayers
	}

}