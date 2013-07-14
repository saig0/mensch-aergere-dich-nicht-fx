package ui.view

import scalafx.scene.Node
import scalafx.scene.layout.HBox
import scalafx.geometry.Insets
import scalafx.geometry.Pos
import scalafx.scene.text.Text
import scalafx.scene.effect.Reflection
import scalafx.scene.paint.LinearGradient
import scalafx.scene.paint.Stop
import scalafx.scene.paint.Color
import scalafx.scene.effect.DropShadow

class TitleView extends HBox with ScalaFxView {

	padding = Insets(20)

	alignment = Pos.CENTER

	content = List(
		new Text {
			text = "Mensch ärgere dich nicht"
			id = "h1"
			effect = new Reflection
			fill = new LinearGradient(
				endX = 0,
				stops = List(Stop(0, Color.ORANGE), Stop(1, Color.CHOCOLATE)))
		},
		new Text {
			text = " FX"
			id = "h1"
			fill = new LinearGradient(
				endX = 0,
				stops = List(Stop(0, Color.CYAN), Stop(1, Color.DODGERBLUE)))
			effect = new Reflection {
				effect = new DropShadow {
					color = Color.DODGERBLUE
					radius = 25
					spread = 0.25
				}
			}
		}
	)

}