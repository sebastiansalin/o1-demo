package s1.demo.rendering

import scala.math.pow

// Vec4 is a 4-dimensional vector, of which the w-component is just a position/direction toggle
case class Vec4(x: N, y: N, z: N, w: N = 1) { // w = 1 => position, w = 0 => direction
  lazy val length: N = pow(x, 2) + pow(y, 2) + pow(z, 2) // w is just an indicator, not a vector value

  def normalized(): Vec4 = this / length

  def /(value: N): Vec4 = {
    Vec4(
      x / value,
      y / value,
      z / value,
      w
    )
  }
}