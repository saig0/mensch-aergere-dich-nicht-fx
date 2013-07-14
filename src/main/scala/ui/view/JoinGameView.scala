package ui.view
import scalafx.scene.layout._
import scalafx.scene.effect._
import scalafx.scene.paint._
import scalafx.scene.control.Label
import scalafx.scene.control.Button
import scalafx.geometry.Pos
import scalafx.scene.control.ProgressIndicator
import dispatch._
import dispatch.Defaults._
import scalafx.scene.control.TableView
import javafx.event.ActionEvent
import javafx.scene.input.MouseEvent
import scalafx.scene.control.TableView.sfxTableView2jfx
import ui.presenter.JoinGamePresenter
import model.Game
import scalafx.scene.control.TableColumn

class JoinGameView(presenter: JoinGamePresenter) extends AbstractScene {

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

	def showGames(games: List[Game]) {
		data = games
		table.setItems(data)
		joinButton.disable = true

		show {
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
		}
	}
}