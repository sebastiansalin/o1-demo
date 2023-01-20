package s1.demo.rendering.objects

import o1.gui.Color
import s1.demo.rendering.{CoordTransform, Index, Object, Vertex}

// The Blender Cube
class Cube(transform: CoordTransform, vertexColor: Color, indexColor: Color) extends Object(
  Vector(
    Vertex(1.0, 1.0, -1.0, vertexColor),
    Vertex(1.0, -1.0, -1.0, vertexColor),
    Vertex(1.0, 1.0, 1.0, vertexColor),
    Vertex(1.0, -1.0, 1.0, vertexColor),
    Vertex(-1.0, 1.0, -1.0, vertexColor),
    Vertex(-1.0, -1.0, -1.0, vertexColor),
    Vertex(-1.0, 1.0, 1.0, vertexColor),
    Vertex(-1.0, -1.0, 1.0, vertexColor),
  ), Vector(
    Index(0 -> 1, indexColor),
    Index(0 -> 2, indexColor),
    Index(0 -> 4, indexColor),
    Index(3 -> 1, indexColor),
    Index(3 -> 2, indexColor),
    Index(4 -> 5, indexColor),
    Index(4 -> 6, indexColor),
    Index(5 -> 1, indexColor),
    Index(6 -> 2, indexColor),
    Index(7 -> 3, indexColor),
    Index(7 -> 5, indexColor),
    Index(7 -> 6, indexColor),
  ), transform
)