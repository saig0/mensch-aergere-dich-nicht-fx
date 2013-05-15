package model

import javafx.beans.property.SimpleStringProperty
import scalafx.beans.property.ObjectProperty

case class Player(name_ : String) {
	val name = ObjectProperty(this, "Name", name_)
}