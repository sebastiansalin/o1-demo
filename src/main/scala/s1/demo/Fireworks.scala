package s1.demo

import scala.collection.mutable.Buffer
import scala.util.Random
import scala.math._
import o1._
import o1.gui._


//THIS EFFECT WAS DONE AFTER THE DEADLINE. IF THAT'S A PROBLEM, DON'T GRADE THIS EFFECT.

//creates a bunch of balls representing the fireworks explosion
class Explosion(val pos: Pos, val power: Double, val col: Color, val amount: Int,val startTime: Double){

  var balls = Buffer[Ball]()

  for(i <- 1 to amount){
    val ball = new Ball(pos, power, col)
    balls += ball
  }

}

//balls that appear in the explosion, they wary in size, speed and direction.
class Ball(initPos: Pos, power: Double, val initColor: Color){
  var col = initColor

  var randomAngle = Fireworks.rand.nextDouble()*2*Pi
  var speed = Fireworks.rand.nextDouble()*power
  var dirX = cos(randomAngle)
  var dirY = sin(randomAngle)
  val radiusAmp = Fireworks.rand.nextInt(3)+1
  var radius = radiusAmp + (power/3).toInt
  var posXY = (initPos.x, initPos.y)

  def pos = new Pos(posXY._1.toInt, posXY._2.toInt)

}


object Fireworks extends Effect(1000, 1000) {
  val length = 1000
  override val height = 1000

  val rand = new scala.util.Random(1)
  val ballAmount = 200
  val explosions = Buffer[Explosion]()


  var time = 0.0
  var timeSpeed = 1.0
  val explosionAmp = 6 // how many explosions happen
  val fadeTime = (timeSpeed*35).toInt

  def makeExplosion() = {
    //random colors for the explosion
    val cR = rand.nextInt(255)
    val cG = rand.nextInt(255)
    val cB = rand.nextInt(255)

    val posX = rand.nextInt(length-200) + 100 //explosions happen at random locations in the frame.
    val posY = rand.nextInt(height-200) + 100

    val powerAmp = rand.nextInt(24)+6

    val explo = new Explosion(new Pos(posX, posY), timeSpeed * powerAmp, o1.Color(cR,cG,cB), ballAmount, time)
    explosions += explo
  }

  def changeThings() = {
    if(time%((fadeTime/explosionAmp).toInt) == 0){
      if(rand.nextInt(2)== 0) makeExplosion() //explosions happen on random ticks
    }



    for (explo <- explosions){
      if(time > explo.startTime + fadeTime) { //if explosion isnt on the screen anymore, remove it's balls to make program run smoother. (better way would be to delete the whole explosion but that didnt work for some reason)
        explo.balls = Buffer[Ball]()
      } else {
        explo.balls.foreach(ball => ball.posXY = ( (ball.posXY._1 + ball.dirX*ball.speed) ,  (ball.posXY._2 + ball.dirY*ball.speed) ))
        explo.balls.foreach(ball => ball.speed = ball.speed * 0.92)
      }

    }

    //time goes forward after every tick
    time += timeSpeed
  }

  //------- drawing -------//


  override def makePic(): Pic = {
    val background = rectangle(this.width, this.height,  Black)
    var pic = background

    // Draw all the explosions.
    for (explo <- explosions){
      if(explo.power >= 27 && time - explo.startTime < 2){ //makes a flash if explosion is big enough
        pic = rectangle(this.width, this.height, DarkGrey)
      }
      for (ball <- explo.balls) {

        val sTime = explo.startTime
        val cR = ball.initColor.red   * ( -(time - sTime)*(time - sTime)*(time - sTime) * 1/(fadeTime*fadeTime*fadeTime) + 1 ) // function that calculates the brightness of a given explosion based on the time the explosion started
        val cG = ball.initColor.green * ( -(time - sTime)*(time - sTime)*(time - sTime) * 1/(fadeTime*fadeTime*fadeTime) + 1 )
        val cB = ball.initColor.blue  * ( -(time - sTime)*(time - sTime)*(time - sTime) * 1/(fadeTime*fadeTime*fadeTime) + 1 )

        val circ = circle(ball.radius, o1.Color(cR, cG, cB))
        pic = circ.against(pic, ball.pos)

      }
    }




    pic
  }



  // This effect will never end
  def next = time > 1000
}
