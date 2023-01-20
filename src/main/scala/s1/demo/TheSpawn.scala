package s1.demo

import scala.math._
import o1._
import o1.gui.Pic
import s1.demo.TheSpawn.{OpenGLTransform, aspectRatio, rotationAxisX, rotationAxisY, rotationAxisZ}
import s1.demo.rendering.objects.{Cube, Pyramid, Wedge}
import s1.demo.rendering.{Matrix4, N, Quaternion, Vec4}

// The Spawn visualizes the creation of the spaceship
// All rendering and demo code by Dennis Marttinen

abstract class Stage(duration: Int) {
  protected var count = 0

  final def update(): Int = {
    change()
    count += 1
    if (count < duration) 0 else 1
  }

  def change()
  def render(base: Pic): Pic
}

// The renderer calls one render cycle before performing any updates, which
// causes the states to render non-projected geometry. This blanket exists
// as a buffer to catch the first render.
object BlanketStage extends Stage(0) {
  override def change(): Unit = Unit
  override def render(base: Pic): Pic = rectangle(base.width, base.height, White)
}

// The Spawn consists of five distinct "stages" which are played back one after another
// Each stage has a set of objects that are transformed, scaled and rotated using some
// matrices in the change() method and then rendered out as layers of indices and vertices
// in the render() method. The abstract base class Stage provides a counter which is used
// to perform various color changes and transforms based on the "time", such as moving the
// object into/out of view on the start/end of the stage.

// The various multipliers and values (magic numbers) in the stages are for artistic purposes only,
// they serve no labeled, critical purpose whatsoever. It's just what I found looked cool.

object Stage1 extends Stage(400) {
  private var baseColor = White
  private val cube = new Cube(OpenGLTransform, Black, Black)
  private var projectionMatrix = Matrix4.newIdentity()
  private val baseTranslation = Matrix4.newTranslation(0, 0, 3)
  private var outTranslation = Matrix4.newIdentity()
  private val scaleMatrix = Matrix4.newScale(0.5, 0.5, 0.5)

  private val moveOut = 300

  override def change(): Unit = {
    projectionMatrix = Matrix4.newPerspective(toRadians(45 * atan(count * 0.03)), aspectRatio)

    if (count > moveOut) {
      outTranslation = Matrix4.newTranslation(0, exp((count - moveOut) * 0.03) - 1.0, 0)
    }

    if (count == 140 || count == 160) {
      baseColor = Black
      cube.setIndexColor(Red)
      cube.setVertexColor(White)
    } else if (count == 145) {
      baseColor = White
      cube.setIndexColor(Green)
      cube.setVertexColor(Blue)
    }

    cube.updateProjection(
      projectionMatrix *
        outTranslation *
        baseTranslation *
        Quaternion(rotationAxisX, count * 0.05).toRotationMatrix *
        Quaternion(rotationAxisY, count * 0.05).toRotationMatrix *
        Quaternion(rotationAxisZ, count * 0.05).toRotationMatrix *
        scaleMatrix
    )
  }

  override def render(base: Pic): Pic = {
    cube.renderVertices(cube.renderIndices(rectangle(base.width, base.height, baseColor)))
  }
}

object Stage2 extends Stage(400) {
  private val baseColor = Black
  private val pyramid = new Pyramid(OpenGLTransform, Black, White)
  private var projectionMatrix = Matrix4.newIdentity()
  private val baseTranslation = Matrix4.newTranslation(0, 0, 3)
  private var outTranslation = Matrix4.newIdentity()
  private val scaleMatrix = Matrix4.newScale(0.5, 0.5, 0.5)

  private val vertexColors = Map(140 -> Red, 150 -> Yellow, 160 -> Green, 170 -> Blue, 180 -> Gold)

  private val moveOut = 300

  override def change(): Unit = {
    projectionMatrix = Matrix4.newPerspective(toRadians(45 * atan(count * 0.03)), aspectRatio)

    if (count > moveOut) {
      outTranslation = Matrix4.newTranslation(0, exp((count - moveOut) * 0.03) - 1.0, 0)
    }

    vertexColors.get(count) match {
      case Some(color) => pyramid.setVertexColor(color)
      case None => ()
    }

    pyramid.updateProjection(
      projectionMatrix *
        outTranslation *
        baseTranslation *
        Quaternion(rotationAxisX, count * 0.05).toRotationMatrix *
        Quaternion(rotationAxisY, -count * 0.05).toRotationMatrix *
        Quaternion(rotationAxisZ, -count * 0.05).toRotationMatrix *
        scaleMatrix
    )
  }

  override def render(base: Pic): Pic = {
    pyramid.renderVertices(pyramid.renderIndices(rectangle(base.width, base.height, baseColor)))
  }
}

object Stage3 extends Stage(400) {
  private var baseColor = Black
  private val wedge = new Wedge(OpenGLTransform, Black, HotPink)
  private var projectionMatrix = Matrix4.newIdentity()
  private val baseTranslation = Matrix4.newTranslation(0, 0, 3)
  private var outTranslation = Matrix4.newIdentity()
  private val scaleMatrix = Matrix4.newScale(0.5, 0.5, 0.5)

  private val indexColors = Map(110 -> Green, 120 -> Cyan, 130 -> Lime, 140 -> Red, 150 -> BrightRed)

  private val moveOut = 300

  override def change(): Unit = {
    projectionMatrix = Matrix4.newPerspective(toRadians(45 * atan(count * 0.03)), aspectRatio)

    if (count > moveOut) {
      outTranslation = Matrix4.newTranslation(0, exp((count - moveOut) * 0.03) - 1.0, 0)
    }

    indexColors.get(count) match {
      case Some(color) => wedge.setIndexColor(color)
      case None => ()
    }

    if (count == 140 || count == 150) {
      baseColor = White
    } else if (count == 148) {
      baseColor = Black
    }

    wedge.updateProjection(
      projectionMatrix *
        outTranslation *
        baseTranslation *
        Quaternion(rotationAxisX, -count * 0.05).toRotationMatrix *
        Quaternion(rotationAxisY, -count * 0.05).toRotationMatrix *
        Quaternion(rotationAxisZ, -count * 0.05).toRotationMatrix *
        scaleMatrix
    )
  }

  override def render(base: Pic): Pic = {
    wedge.renderVertices(wedge.renderIndices(rectangle(base.width, base.height, baseColor)))
  }
}

object Stage4 extends Stage(650) {
  private var baseColor = White

  private val cube = new Cube(OpenGLTransform, Red, Black)
  private val pyramid = new Pyramid(OpenGLTransform, Green, Black)
  private val wedge = new Wedge(OpenGLTransform, Blue, Black)

  private var projectionMatrix = Matrix4.newIdentity()
  private val baseTranslation = Matrix4.newTranslation(0, 0, 3)
  private var outScale = Matrix4.newIdentity()
  private val scaleMatrix = Matrix4.newScale(0.4, 0.4, 0.4)

  private val moveOut = 550

  private def spin(speedMul: N, amountMul: N, offset: N) = {
    val x = (count * speedMul).min(4.0 * Pi) + offset
    val spin = Matrix4.newTranslation(sin(x) * amountMul, cos(x) * amountMul, 0)

    if (count > 400) {
      val centerMove = ((count - 400) / 100.0).min(1.0)
      spin * Matrix4.newTranslation(-sin(x) * amountMul * centerMove, -cos(x) * amountMul * centerMove, 0)
    } else {
      spin
    }
  }

  override def change(): Unit = {
    projectionMatrix = Matrix4.newPerspective(toRadians(45 * atan(count * 0.03)), aspectRatio)

    if (count > moveOut) {
      val x = ((count - moveOut) * 0.03).min(1.0)
      val scale = 2 * (x - 1) / (x - 2)
      outScale = Matrix4.newScale(scale, scale, scale)

      if (scale < 0.001) {
        // Hides the remaining dots
        cube.setVertexColor(Black)
        pyramid.setVertexColor(Black)
        wedge.setVertexColor(Black)
        baseColor = Black
      }
    }

    cube.updateProjection(
      projectionMatrix *
        baseTranslation *
        outScale *
        spin(0.05, 1.0, 0) *
        Quaternion(rotationAxisX, count * 0.05).toRotationMatrix *
        Quaternion(rotationAxisY, count * 0.05).toRotationMatrix *
        Quaternion(rotationAxisZ, count * 0.05).toRotationMatrix *
        scaleMatrix
    )

    pyramid.updateProjection(
      projectionMatrix *
        baseTranslation *
        outScale *
        spin(0.05, 1.0, 4.0/3.0 * Pi) *
        Quaternion(rotationAxisX, count * 0.05).toRotationMatrix *
        Quaternion(rotationAxisY, -count * 0.05).toRotationMatrix *
        Quaternion(rotationAxisZ, -count * 0.05).toRotationMatrix *
        scaleMatrix
    )

    wedge.updateProjection(
      projectionMatrix *
        baseTranslation *
        outScale *
        spin(0.05, 1.0, 8.0/3.0 * Pi) *
        Quaternion(rotationAxisX, -count * 0.05).toRotationMatrix *
        Quaternion(rotationAxisY, -count * 0.05).toRotationMatrix *
        Quaternion(rotationAxisZ, -count * 0.05).toRotationMatrix *
        scaleMatrix
    )
  }

  override def render(base: Pic): Pic = {
    cube.renderVertices(cube.renderIndices(
      pyramid.renderVertices(pyramid.renderIndices(
        wedge.renderVertices(wedge.renderIndices(rectangle(base.width, base.height, baseColor)))
      ))
    ))
  }
}

object Stage5 extends Stage(720) {
  private var baseColor = Black

  // This stage constructs a multi-component spaceship that is then
  // handled as a single, full object using the individual
  // projections for each part.

  private val shipParts = Map(
    "body" -> new Cube(OpenGLTransform, Black, White),
    "rightWing" -> new Wedge(OpenGLTransform, Black, White),
    "leftWing" -> new Wedge(OpenGLTransform, Black, White),
    "topWing" -> new Wedge(OpenGLTransform, Black, White),
    "cockpit" -> new Pyramid(OpenGLTransform, Black, White),
  )

  private val projections = Map(
    "body" -> Matrix4.newScale(2.0, 1.0, 1.0),
    "rightWing" -> Matrix4.newTranslation(-0.5, 0.0, -2.0) * Quaternion(Vec4(1.0, 0.0, 0.0), toRadians(-90)).toRotationMatrix,
    "leftWing" -> Matrix4.newTranslation(-0.5, 0.0, 2.0) * Quaternion(Vec4(1.0, 0.0, 0.0), toRadians(90)).toRotationMatrix,
    "topWing" -> Matrix4.newTranslation(-0.5, 2.0, 0.0),
    "cockpit" -> Matrix4.newTranslation(3.0, 0.0, 0.0) * Quaternion(Vec4(0.0, 0.0, 1.0), toRadians(-90)).toRotationMatrix,
  )

  private var projectionMatrix = Matrix4.newIdentity()
  private val baseTranslation = Matrix4.newTranslation(0, 0, 3)
  private val scaleMatrix = Matrix4.newScale(0.4, 0.4, 0.4)
  private var outTranslation = Matrix4.newIdentity()

  private val indexColors = Map(200 -> Green, 210 -> Cyan, 220 -> Lime, 230 -> Red, 240 -> HotPink)
  private val moveOut = 650

  private def projectSpaceship(matrix: Matrix4): Unit = {
    for (part <- shipParts.keys) {
      shipParts(part).updateProjection(
        matrix * projections(part)
      )
    }
  }

  private def inScale(speed: Double): Matrix4 = {
    val x = count * speed
    val scaleMul = if (x < Pi / 2.0) sin(x) else 1.0
    Matrix4.newScale(scaleMul, scaleMul, scaleMul)
  }

  override def change(): Unit = {
    projectionMatrix = Matrix4.newPerspective(toRadians(45 * atan(count * 0.03)), aspectRatio)

    indexColors.get(count) match {
      case Some(color) => shipParts.values.foreach(_.setIndexColor(color))
      case None => ()
    }

    if (count == 330) {
      baseColor = White
      shipParts.values.foreach(p => {
        p.setVertexColor(Red)
        p.setIndexColor(Black)
      })
    } else if (count == 400) {
      baseColor = Black
      shipParts.values.foreach(p => {
        p.setVertexColor(Black)
        p.setIndexColor(White)
      })
    }

    if (count > moveOut) {
      val x = ((count - moveOut) * 0.015).min(1.0)
      val mul = pow(x, 2)
      outTranslation = Matrix4.newTranslation(0.0, 0.0, mul * 100.0)
    }

    projectSpaceship(
      projectionMatrix *
        baseTranslation *
        outTranslation *
        inScale(0.007) *
        Quaternion(rotationAxisX, count * 0.03).toRotationMatrix *
        Quaternion(rotationAxisY, -count * 0.05).toRotationMatrix *
        Quaternion(rotationAxisZ, count * 0.04).toRotationMatrix *
        scaleMatrix
    )
  }

  override def render(base: Pic): Pic = {
    shipParts.values.foldLeft(rectangle(base.width, base.height, baseColor))((pic, part) => {
      part.renderVertices(part.renderIndices(pic))
    })
  }
}

object TheSpawn extends Effect(1000, 1000) {
  // Makes the y-coordinate start from the bottom and increase upwards and transforms the coordinates
  // to OpenGL screen-space, so (-1, -1) is the bottom-left corner and (0, 0) is the center
  def OpenGLTransform(x: N, y: N) = Pos(width * (x + 1.0) / 2.0, 0.5 * height * (1.0 - y))
  def toRadians(degrees: N): N = degrees * math.Pi / 180

  val aspectRatio: N = width.toDouble / height.toDouble
  val rotationAxisX: Vec4 = Vec4(1.0, 0.0, 0.0)
  val rotationAxisY: Vec4 = Vec4(0.0, 1.0, 0.0)
  val rotationAxisZ: Vec4 = Vec4(0.0, 0.0, 1.0)

  private val backdrop = emptyCanvas(width, height, Black)
  private val stages = Vector(BlanketStage, Stage1, Stage2, Stage3, Stage4, Stage5)
  private var (stage, rendering) = (0, 0)
  private var finished = false

  /**
   * Changes the state of the effect. Similar to the tick method in Flappy
   */
  override def changeThings(): Unit = {
    if (stage < stages.length)
      stage += stages(stage).update()
    else
      finished = true
  }

  /**
   * Creates an os.gui.Pic for the current state of the effect
   */
  override def makePic(): Pic = {
    if (rendering < stages.length) {
      val result = stages(rendering).render(backdrop)
      // changeThings switches to the next stage, but we still need to render the
      // final image of the current stage, the rendering counter keeps track of that
      if (rendering < stage) rendering = stage
      result
    } else {
      backdrop
    }
  }

  override def next: Boolean = finished
}