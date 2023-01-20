package s1.demo.rendering.objects

import o1.gui.Color
import s1.demo.rendering.{CoordTransform, Index, Object, Vertex}

// A wedge with a square base
class Wedge(transform: CoordTransform, vertexColor: Color, indexColor: Color) extends Object(
  Vector(
    Vertex(-1.0, -1.0, -1.0, vertexColor),
    Vertex(-1.0, -1.0, 1.0, vertexColor),
    Vertex(1.0, -1.0, 1.0, vertexColor),
    Vertex(1.0, -1.0, -1.0, vertexColor),
    Vertex(-1.0, 1.0, 0.0, vertexColor),
    Vertex(1.0, 1.0, 0.0, vertexColor),
  ), Vector(
    Index(0 -> 1, indexColor),
    Index(1 -> 2, indexColor),
    Index(2 -> 3, indexColor),
    Index(3 -> 0, indexColor),
    Index(0 -> 4, indexColor),
    Index(1 -> 4, indexColor),
    Index(2 -> 5, indexColor),
    Index(3 -> 5, indexColor),
    Index(4 -> 5, indexColor),
  ), transform
)