package s1.demo

import o1._
import o1.gui.Pic

import scala.collection.mutable.Buffer
import scala.util.Random

/**
 * This is the base class for every demo effect you implement.
 * 
 * @constructor Creates a new demo effect. Effects are shown in the [[DemoArea]].
 * @param width width of the effect in pixels
 * @param height height of the image in pixels
 */
object Starfield extends Effect(1000, 1000) {
  private val numberOfStars = 200
  private val starDiameterMin = 5
  private val starDiameterMax = 20
  private val starSpeedMin = 1.5
  private val starSpeedMax = 30.0
  private val numberOfNewStars = 1
  private val relativeRadiusOfNewStars = 0.01

  private val colors = Vector[Color](White, Blue, Red, Green, Purple, Yellow)

  private val halfWidth = this.width / 2
  private val halfHeight = this.height / 2

  private val middlePoint = Pos(halfWidth, halfHeight)
  private val maxDistance = math.sqrt(halfWidth*halfWidth + halfHeight*halfHeight)

  private var stars: Buffer[Star] = initStars(this.numberOfStars)

  private var counter = 0
      
  /**
   * Changes the state of the effect. Similar to the tick method in Flappy
   */
  def changeThings(): Unit = {
    stars = stars.map(calculateNewCoordinates) ++ initStars(numberOfNewStars, relativeRadiusOfNewStars)
    stars = stars.filter(_.pos.distance(middlePoint) < maxDistance)

    this.counter += 1
  }

  def calculateNewCoordinates(star: Star): Star = {
    val starPos = Pos(star.pos.x, star.pos.y)

    val starSpeed = mapNumbers(starPos.distance(middlePoint).abs, 0, maxDistance, starSpeedMin, starSpeedMax)
    val newPos = starPos.nextPos(Velocity(middlePoint.directionOf(starPos), starSpeed))

    val newDiameter = mapNumbers(starPos.distance(middlePoint).abs, 0, maxDistance, starDiameterMin, starDiameterMax).toInt

    Star(newPos, star.color, newDiameter)
  }
  
  /**
   * Creates an os.gui.Pic for the current state of the effect
   */
  
  def makePic(): Pic = {
    val background = rectangle(this.width, this.height,  Black)
    var pic = background

    for (star <- this.stars) {
      pic = pic.place(o1.star(star.diameter, star.color), Pos(star.pos.x, star.pos.y))
    }

    pic
  }

  // Create stars at random positions
  private def initStars(amount: Int, max: Double = 1.0): Buffer[Star] = {
    var amountToCreate = amount
    val stars = Buffer[Star]()

    if (amountToCreate < 0 && counter % amountToCreate.abs != 0) amountToCreate = 0 else if (amountToCreate < 0) amountToCreate = 1

    for (x <- 0 until amountToCreate) {
      val x = Random.nextInt((this.width * max).toInt) + (this.width * (1 - max) / 2).toInt
      val y = Random.nextInt((this.height * max).toInt) + (this.width * (1 - max) / 2).toInt

      val color = this.colors(Random.nextInt(this.colors.length))
      
      stars += Star(Pos(x, y), color, starDiameterMin)
    }

    stars
  }

  // Map distances from middle point to speeds to achieve acceleration which that creates the depth effect
  def mapNumbers(x: Double, inMin: Double, inMax: Double, outMin: Double, outMax: Double): Double = (x - inMin) * (outMax - outMin) / (inMax - inMin) + outMin
  
  /**
   * This method tells the Demo engine when it is time to switch to the next effect
   * If this is the only effect, the method always returns '''false'''
   * 
   * @return true when this effect is over, false otherwise
   */
  override def next: Boolean = this.counter >= 450
}

case class Star(pos: Pos, color: Color, diameter: Int)