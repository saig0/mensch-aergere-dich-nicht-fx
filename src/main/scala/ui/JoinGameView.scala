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
import javafx.scene.input.MouseEvent

class JoinGameView(presenter: JoinGamePresenter) extends AbstractScene {

	private lazy val c = new HBox {
		alignment = Pos.CENTER
		spacing = 20
	}

	private lazy val joinButton = new Button {
		text = "Spiel beitreten"
		disable = true
		onAction = (event: ActionEvent) => presenter.joinGame
	}

	private lazy val table: TableView[Game] = new TableView[Game] {
		columns += new TableColumn[Game, String] {
			text = "IP"
			cellValueFactory = { _.value.address }
		}
		columns += new TableColumn[Game, String] {
			text = "Spieler"
			cellValueFactory = { _.value.currentPlayers.toString }
		}
		columns += new TableColumn[Game, String] {
			text = "max. Spieler"
			cellValueFactory = { _.value.maxPlayers.toString }
		}

		onMouseClicked = (event: MouseEvent) => {
			if (table.getSelectionModel.getSelectedItems.size > 0) {
				joinButton.disable = false
			} else {
				joinButton.disable = true
			}
		}
	}

	def selectedGame = table.getSelectionModel.getSelectedItem

	private var data = List[Game]()

	def contentCenter = {
		c.content = List(
			new Label {
				text = "Lade Spiele vom Game-Server"
			},
			new ProgressIndicator {
				progress = -1
			}
		)
		List(c)
	}

	def showLoading = contentCenter

	def showGames(games: List[Game]) {
		data = games
		table.setItems(data)
		joinButton.disable = true

		c.content = List(
			new HBox {
				spacing = 50
				content = List(
					new VBox {
						spacing = 20
						content = List(
							joinButton,
							new Button {
								text = "aktuallisieren"
								onAction = (event: ActionEvent) => presenter.loadGames
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
}