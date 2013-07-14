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
import ui.presenter.GameCreationPresenter
import model.Player
import scalafx.scene.control.TableColumn
import model.Game

class GameCreationView(presenter: GameCreationPresenter) extends AbstractScene {

	private lazy val table: TableView[Player] = new TableView[Player] {
		columns += new TableColumn[Player, String] {
			text = "Spieler"
			cellValueFactory = { _.value.name }
		}

		onMouseClicked = (event: MouseEvent) => {
			if (table.getSelectionModel.getSelectedItems.size > 0) {
				removePlayerButton.disable = false
			} else {
				removePlayerButton.disable = true
			}
		}
	}

	private lazy val playersLabel = new Label {
		text = "Spieler: " + 0 + " / " + 0
	}

	private lazy val startGameButton = new Button {
		text = "Spiel starten"
		disable = true
		onAction = (event: ActionEvent) => presenter.startGame
	}

	private lazy val addCpuPlayerButton = new Button {
		text = "CPU hinzufügen"
		disable = false
		onAction = (event: ActionEvent) => presenter.newCpuPlayer
	}

	private lazy val removePlayerButton = new Button {
		text = "Spieler rauswerfen"
		disable = true
		onAction = (event: ActionEvent) => presenter.removePlayer
	}

	private var data = List[Player]()

	private var game: Game = _

	def showGame(game: Game) {
		this.game = game
		data = Nil

		show {
			List(
				new VBox {
					spacing = 20
					content = List(
						new Label {
							text = "IP: " + game.address
						},
						playersLabel,
						startGameButton,
						addCpuPlayerButton,
						removePlayerButton,
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

	def joinPlayer(player: Player) {
		data ++= player :: Nil
		updateTableData(data)
	}

	def removePlayer(player: Player) {
		data = (data.toBuffer - player).toList
		updateTableData(data)
	}

	private def updateTableData(data: List[Player]) {
		table.setItems(data)

		playersLabel.text = "Spieler: " + data.size + " / " + game.maxPlayers
		startGameButton.disable = data.size < game.maxPlayers
		addCpuPlayerButton.disable = data.size >= game.maxPlayers

	}

	def selectedPlayer = table.getSelectionModel.getSelectedItem
}