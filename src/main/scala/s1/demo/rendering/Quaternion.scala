package s1.demo.rendering

import scala.math.{sin,cos}

// A quaternion is an extension to complex numbers, this basically enables
// rotation in 3D-space while avoiding the gimbal lock problem of euclidean rotation
class Quaternion(rotationAxis: Vec4, angle: N) {
  private val axis = rotationAxis.normalized()

  lazy val x: N = axis.x * sin(angle / 2)
  lazy val y: N = axis.y * sin(angle / 2)
  lazy val z: N = axis.z * sin(angle / 2)
  lazy val w: N = cos(angle / 2)

  // Construct a rotation matrix from the quaternion that can be used to rotate vectors
  def toRotationMatrix: Matrix4 = Matrix4(
    1.0 - 2.0*y*y - 2.0*z*z, 2.0*x*y - 2.0*z*w, 2.0*x*z + 2.0*y*w, 0.0,
    2.0*x*y + 2.0*z*w, 1.0 - 2.0*x*x - 2.0*z*z, 2.0*y*z - 2.0*x*w, 0.0,
    2.0*x*z - 2.0*y*w, 2.0*y*z + 2.0*x*w, 1.0 - 2.0*x*x - 2.0*y*y, 0.0,
    0.0, 0.0, 0.0, 1.0,
  )
}

object Quaternion {
  def apply(rotationAxis: Vec4, angle: N): Quaternion = new Quaternion(rotationAxis, angle)
  def newIdentity(): Quaternion = Quaternion(Vec4(1.0, 1.0, 1.0), 0)
}