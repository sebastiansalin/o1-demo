package s1.demo

import scala.collection.mutable.Queue
import o1._
import o1.gui._

case class Circle(x: Int, y: Int) {
  //Circles do not like being too close to each other. 
  def tooNear(other: Circle) = math.hypot(this.x - other.x, this.y - other. y) < 5
}

/**
 * The idea for this effect came from Felix Bade.
 * 
 * The effect draws a continuous stream of filled
 * circles that changes it's course randomly.
 */
object Snakes extends Effect(500, 500) {
      
    // The circles we draw are in a queue.The latest is
    // always stored in [[last]]
    var last        = new Circle(100, 100)
    val circles     = Queue[Circle](last)
    var direction   = 0.0 
    val step        = 10
    var queueLength = 1
    
    val random = new util.Random
    
    def changeThings() = {
      // Change the direction where to draw the next circle randomly
      direction = direction + (random.nextDouble - 0.5);
      
      // Calculate the new coordinates 
      val xdiff = (math.cos(direction) * step).toInt
      val ydiff = (math.sin(direction) * step).toInt
      
      val x = (last.x + xdiff + 500) % 500
      val y = (last.y + ydiff + 500) % 500
      last  = new Circle(x,y)
      
      // Store the circle in a queue (Think of a buffer where you add in one end and take stuff out from the other)
      circles.enqueue( last )
      queueLength += 1
      
      // If the queue gets big, we delete older circles for a fun effect
      while (queueLength > 400) {
        circles.dequeue()
        queueLength -= 1
      }
    }
    
    //------- drawing -------//
    
    
    override def makePic(): Pic = {
      val background = rectangle(this.width, this.height,  DarkBlue)
      var pic = background
      // Draw all the circles.
      

      for (c <- circles) {
        val radius = 20
        val snakePart = circle(radius*2, White).place(circle(radius*2, Black), new Pos(radius - 2,radius - 2))
        
        pic = snakePart.against(pic, new Pos(c.x - radius, c.y - radius))
        
      }

      pic
    }
    
    
    // Effects can also receive information on mouse movements.
    // When a mouse goes to ne coordinates this method gets called.
    
    // We use it to draw still more circles at the mouse location
    
    // The lastMouseCircle trick is there to prevent accidentally drawing a lot of circles when moving the mouse very slowly
    var lastMouseCircle:Option[Circle] = None
    
    override def mouseAt(x: Int, y: Int) = {
      val newCircle = new Circle(x,y)
      if (!lastMouseCircle.exists(_ tooNear newCircle)) {
         lastMouseCircle = Some(newCircle)
         circles enqueue newCircle
      }
    }
 
    // This effect will never end
    def next = false
  }
