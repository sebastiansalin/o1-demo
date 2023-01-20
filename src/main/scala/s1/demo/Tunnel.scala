package s1.demo

import scala.collection.mutable.Queue
import o1._
import o1.gui._
import scala.collection.mutable.Buffer
import scala.util.Random
import scala.math._

//this class reprsents all circles
class Rim(initialpos: Pos, initialcolor: Color, val startTime: Int){
  var pos = initialpos
  var radius = 0.0
  var color = initialcolor
  var speed = 1.0
}

object Tunnel extends Effect(1000, 1000) {

  var size = 1000
  var rand = new Random(255) //number is the seed. Seed will influence the colors and the movement of the "tunnel"
  val circles = Buffer[Rim]()
  var circleAmount = 40 //how many circles are rendered
  var startTimeAmp = 50 //this modifies the speed at which new circles appear on the screen
  var spread = 190 //this modifies how much the tunnel turns


  var lastMid = (size/2, size/2)
  var lastColor = o1.Color(0,0,0)
  var xdir = rand.nextDouble()
  var ydir = rand.nextDouble()

  def newDir() = {
    var amp = 1.0/6
    val y = (rand.nextDouble * 2 - 1) * amp
    val x = (rand.nextDouble * 2 - 1) * amp

    xdir = min(max(xdir+x,-1),1)
    ydir = min(max(ydir+y,-1),1)
  }

  def createCircle(startTime: Int) = {
    val r = lastColor.red + rand.nextInt(70)-29
    val g = lastColor.green + rand.nextInt(70)-32
    val b = lastColor.blue + rand.nextInt(70)-32



    val col = o1.Color.apply(r,g,b)
    lastColor = col

    newDir()

    val startX = (lastMid._1 + xdir * spread).toInt/2 + size/4
    val startY = (lastMid._2 + ydir * spread).toInt/2 + size/4

    lastMid = (startX, startY)

    circles += new Rim(new Pos(startX,startY), col, startTime)
  }

  for(i <- 1 to circleAmount){ //creates all circles
    val startTime = i * startTimeAmp
    createCircle(startTime)
  }


  var time = 0.0
  var timeSpeed = 5.0
  var timeAcc = 0.02
  var endTime = 15000
  var time2 = 0.0


  def changeThings() = {

    if(time < endTime){ //tunnel effect
      time += timeSpeed
      timeSpeed += timeAcc
      for(c <- circles){
        if(c.radius > size*7){ //if circle is out of view, delete it and create a new one that will appear on sreen soon
          circles -= c

          val startTime = circles.last.startTime + startTimeAmp
          createCircle(startTime)
        } else if(time >= c.startTime){
          c.radius = 0.01*(time - c.startTime)*(time - c.startTime)


        } else {

        }
      }
    } else { //fading effect and end
      time2 += 10
    }

  }


  //------- drawing -------//


  override def makePic(): Pic = {
    if(time < endTime){
      val background = rectangle(this.width, this.height,  Black)
      var pic = background

      // Draw all the circles.

      for(c <- circles){
        var circ1 = circle(c.radius, c.color)

        //circles seperated by black "background"
        var blackCirc = circle(c.radius*0.85-0.001*time, Black)
        //var blackCirc = circle(c.radius*0.85, Black)


        //var blackCirc = circle(c.radius-50, Black)

        var blackPos = new Pos(c.radius/2, c.radius/2)
        pic = circ1.place(blackCirc, blackPos).against(pic, c.pos)

      }
      pic
    } else { //ending flash
      rectangle(this.width, this.height, o1.Color(255-time2, 255-time2, 255-time2))
    }
  }



  // This effect will never end when time hits a certain number
  def next = time2 >= 500
}
