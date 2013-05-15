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

class GameCreationScene(player: String) extends AbstractScene {

	lazy val c = new HBox {
		alignment = Pos.CENTER
		spacing = 20
	}

	lazy val table = new TableView[Player] {
		columns += new TableColumn[Player, String] {
			text = "Spieler"
			cellValueFactory = { _.value.name }
		}
	}

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

	def onShow {
		updateUi(GameServerClient.newGame(4, 1), { (game: Game) =>
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
								new Label {
									text = "Spieler: " + game.currentPlayers + " / " + game.maxPlayers
								},
								new Button {
									text = "Spiel starten"
									disable = game.currentPlayers < game.maxPlayers
								}
							)
						},
						table
					)
				}
			)
			table.setItems(List(Player(player)))
		}
		)
	}

}