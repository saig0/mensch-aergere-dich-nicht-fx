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
import javafx.scene.input.KeyEvent

class ConnectGameView(presenter: ConnectGamePresenter) extends AbstractScene {

	private lazy val ipField = new TextField {
	}

	private lazy val connectButton = new Button {
		text = "verbinden"
		onAction = (event: ActionEvent) => presenter.joinGame
	}

	def ip: String = ipField.text.value

	def contentCenter = List(
		new HBox {
			alignment = Pos.CENTER
			spacing = 20
			content = List(
				new Label {
					text = "IP des Spiel-Servers"
				},
				ipField,
				connectButton,
				new Button {
					text = "abbrechen"
					onAction = (event: ActionEvent) => presenter.abort
				}
			)
		}
	)
}