package s1.demo

import o1._

/**
 * Effects is simply a helper class that changes effects when necessary
 *
 * You can modify this class as you wish, but for most purposes it is
 * enough to add your own effects to the Vector "effects"
 */


object Effects {
  val effects = Vector[Effect](TheSpawn, Tunnel, Fireworks, Starfield, Laser)

  var done = false;
  var currentEffect = effects.head
  var nextEffects   = effects.tail

  def tick() = {
    currentEffect.changeThings();

    if (currentEffect.next) {
      if (nextEffects.isEmpty) {
        done = true
      } else {
        currentEffect = nextEffects.head
        nextEffects   = nextEffects.tail
      }
    }
  }

  def makePic   = currentEffect.makePic

  def mouseAt(x: Int, y: Int) = currentEffect.mouseAt(x, y)

  def finished = done

}

/**
 * DemoArea hosts one [[Effect]] at a time.
 *
 * THIS CLASS DOES NOT NEED TO BE MODIFIED
 * OR UNDERSTOOD
 */


object DemoArea extends App {

  val demoArea = new View(Effects, "The Life of a Space Ship") {

    def makePic = model.makePic

    override def onMouseMove(position: Pos) = {
      model.mouseAt(position.x.toInt, position.y.toInt)
    }

    override def onTick() = {
      model.tick()
    }

    override def isDone = model.finished

  }

  demoArea.start()

}
