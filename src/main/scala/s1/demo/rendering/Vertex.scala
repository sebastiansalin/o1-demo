package s1.demo.rendering

import o1.Black
import o1.gui.Color

// A vertex represents a "corner point" of an object
class Vertex(x: N, y: N, z: N, var color: Color) extends Vec4(x, y, z)

object Vertex {
  def apply(x: N, y: N, z: N, color: Color): Vertex = new Vertex(x, y, z, color)
  def apply(vector: Vec4, color: Color): Vertex = new Vertex(vector.x, vector.y, vector.z, color)
  def newBlanket(): Vertex = Vertex(0.0, 0.0, 0.0, Black)
}