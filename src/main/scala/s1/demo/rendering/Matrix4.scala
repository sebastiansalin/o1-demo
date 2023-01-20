package s1.demo.rendering

import scala.math.tan

// Matrix4 is a regular 4-dimensional matrix
case class Matrix4(
             aa: N, ab: N, ac: N, ad: N,
             ba: N, bb: N, bc: N, bd: N,
             ca: N, cb: N, cc: N, cd: N,
             da: N, db: N, dc: N, dd: N,
             ) {

  // Project the given vector using this matrix
  def *(v: Vec4): Vec4 = Vec4(
    aa * v.x + ab * v.y + ac * v.z + ad * v.w,
    ba * v.x + bb * v.y + bc * v.z + bd * v.w,
    ca * v.x + cb * v.y + cc * v.z + cd * v.w,
    da * v.x + db * v.y + dc * v.z + dd * v.w,
  )

  // Matrix4 * Matrix4
  def *(m: Matrix4): Matrix4 = Matrix4(
    aa * m.aa + ab * m.ba + ac * m.ca + ad * m.da, aa * m.ab + ab * m.bb + ac * m.cb + ad * m.db, aa * m.ac + ab * m.bc + ac * m.cc + ad * m.dc, aa * m.ad + ab * m.bd + ac * m.cd + ad * m.dd,
    ba * m.aa + bb * m.ba + bc * m.ca + bd * m.da, ba * m.ab + bb * m.bb + bc * m.cb + bd * m.db, ba * m.ac + bb * m.bc + bc * m.cc + bd * m.dc, ba * m.ad + bb * m.bd + bc * m.cd + bd * m.dd,
    ca * m.aa + cb * m.ba + cc * m.ca + cd * m.da, ca * m.ab + cb * m.bb + cc * m.cb + cd * m.db, ca * m.ac + cb * m.bc + cc * m.cc + cd * m.dc, ca * m.ad + cb * m.bd + cc * m.cd + cd * m.dd,
    da * m.aa + db * m.ba + dc * m.ca + dd * m.da, da * m.ab + db * m.bb + dc * m.cb + dd * m.db, da * m.ac + db * m.bc + dc * m.cc + dd * m.dc, da * m.ad + db * m.bd + dc * m.cd + dd * m.dd,
  )
}

object Matrix4 {
  def newIdentity(): Matrix4 = newScale(1.0, 1.0, 1.0)

  // Construct a new scaling matrix to scale objects' vertices
  def newScale(x: N, y: N, z: N): Matrix4 = Matrix4(
    x,   0.0, 0.0, 0.0,
    0.0, y,   0.0, 0.0,
    0.0, 0.0, z,   0.0,
    0.0, 0.0, 0.0, 1.0,
  )

  // Construct a new translation matrix to translate objects' vertices
  def newTranslation(x: N, y: N, z: N): Matrix4 = Matrix4(
    1.0, 0.0, 0.0, x,
    0.0, 1.0, 0.0, y,
    0.0, 0.0, 1.0, z,
    0.0, 0.0, 0.0, 1.0,
  )

  // Construct a new orthographic projection matrix, takes in an aspect ratio as width/height
  def newOrthographic(aspectRatio: N): Matrix4 = Matrix4(
    // This requires a height/width aspect ratio, but takes in
    // a width/height one for compatibility with perspective
    1.0 / aspectRatio, 0.0, 0.0, 0.0,
    0.0,               1.0, 0.0, 0.0,
    0.0,               0.0, 1.0, 0.0,
    0.0,               0.0, 0.0, 1.0,
  )

  // Construct a new perspective projection matrix with the given FOV and aspect ratio (width/height)
  def newPerspective(fovRadians: N, aspectRatio: N): Matrix4 = {
    val tanHalfFOV = tan(fovRadians / 2.0)

    // This is a basic perspective projection matrix
    Matrix4(
      1.0 / (aspectRatio * tanHalfFOV), 0.0,               0.0,  0.0,
      0.0,                              1.0 / tanHalfFOV,  0.0,  0.0,
      0.0,                              0.0,              -1.0, -1.0,
      0.0,                              0.0,               1.0,  0.0,
    )
  }
}