package s1.demo

import o1.gui.Pos

package object rendering {
  // The numeric type of the rendering math
  type N = Double

  // The coordinate system transformation function header, this is used
  // to transform O1 library's coordinates to the OpenGL screen space
  type CoordTransform = (N, N) => Pos
}