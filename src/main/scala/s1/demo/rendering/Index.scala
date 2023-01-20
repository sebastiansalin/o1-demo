package s1.demo.rendering

import o1.gui.Color

// An index represents the connection between two vertices in an object
case class Index(a: Int, b: Int, var color: Color)

object Index {
  def apply(mapping: (Int, Int), color: Color): Index = Index(mapping._1, mapping._2, color)
}