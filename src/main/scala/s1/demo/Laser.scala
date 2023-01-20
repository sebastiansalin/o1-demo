package s1.demo

import scala.collection.mutable.Queue
import o1._
import o1.gui._

/**
 * The idea for this effect came from Felix Bade.
 *
 * The effect draws a continuous stream of filled
 * circles that changes it's course randomly.
 */
object Laser extends Effect(1000, 1000) {

    var r = width/2.toDouble // säde
    var clock = 0.0 //kello
    var theta = 0.0 //kulma
    var h = this.width/2 //keskipiste x
    var k = this.height/2 //keskipiste y
    var middlePos = new Pos(this.width/2.toDouble,this.height/2.toDouble) //keski pos
    var x = h + r.toDouble //viivan loppupiste arvo x
    var y = k + r.toDouble //viivan loppupiste arvo y
    var startingPos = middlePos.addY(500) // viivan loppus Pos
    var endPos = middlePos.addY(r) //apu metodi
    var endPositions = Array.ofDim[Pos](150) //viivojen määrä
    for(x<- 0 until 150) { //viivojen määrän mukaan
      endPositions(x)= new Pos(0,0)
    }

    // spaceship jutut
    var shipR = width/2.toDouble
    var shipUnit = 25.0
    var shipPos = new Pos(0,0)
    var kolmio = triangle(shipUnit * 2,shipUnit * 2,Yellow,Center)
    var shipX = h + r.toDouble
    var shipY = k + r.toDouble

    //spaceship osia
    var bottom = star(shipUnit,Yellow)
    var laatikko = rectangle(shipUnit,shipUnit*2,Grey)
    var yla = triangle(shipUnit,shipUnit,Grey)
    var ikkuna = circle(shipUnit / 2, Blue)

    var shipPart1 = ikkuna.against(laatikko, new Pos(laatikko.width/2,laatikko.height/2))
    var shipPart2 = shipPart1.onto(bottom,new Pos(bottom.width / 2,-bottom.height / 2))
    var fullShip = shipPart2.below(yla)

    // Alien
    var startSize = 1.0
    var alien = o1.gui.Pic("alien.png").scaleBy(startSize)
    val alienOriginal = o1.gui.Pic("alien.png").scaleBy(startSize)
    var alienR = width/2.toDouble
    var alienX = h + r.toDouble
    var alienY= k + r.toDouble
    var alienPos = new Pos(250, 250)

    //Asteroid
    var asteroidSize = 0.5
    var asteroid = o1.gui.Pic("asteroid.png").scaleBy(asteroidSize)
    var asteroidOriginal = o1.gui.Pic("asteroid.png").scaleBy(asteroidSize)
    var asteroidX = h + r.toDouble
    var asteroidY =k + r.toDouble
    var asteroidR = width/2.toDouble - 100
    var asteroidPos = new Pos(500,500)



    def changeThings() = {
      clock += 0.2
      //lue viivojen loppuspisteitä
      def newEndCoord(luku: Int) = {
        x = h + r*math.cos(theta*4/luku+luku ) // cos(viivojen nopeus)
        y = k + r*math.sin(theta*4/luku+luku ) // sin(viivojen nopeus)
        // pienentää ympyrän pinta-alaa
        /**
        if(!(r < -width/2) ){
            r = r - 0.01
        }
        **/
        endPos = new Pos(x,y)
        endPos
      }

      //testailua
      alienX = h + alienR*math.cos(theta/3+ 5)
      alienY = k + alienR*math.sin(theta/3 + 5)
      alienPos = new Pos(alienX,alienY)
      alienR += -2
      startSize += -0.001
      alien = alien.scaleBy(startSize)

      // asteroid
      asteroidX = h + asteroidR*math.cos(theta/5 + 10)
      asteroidY = k + asteroidR*math.sin(theta/5 + 10)
      asteroidPos = new Pos(asteroidX,asteroidY)
      asteroidR += -2
      asteroidSize += -0.004
      asteroid = asteroidOriginal.scaleBy(asteroidSize)




      // ship positionin asioita
        shipX = h + shipR*math.cos(theta/3)
        shipY = k + shipR*math.sin(theta/3)
        shipPos = new Pos(shipX,shipY)
        shipR += -1
        shipUnit += -0.05
        // ship pienentyminen
        bottom = star(shipUnit,Yellow)
        laatikko = rectangle(shipUnit,shipUnit*2,Grey)
        yla = triangle(shipUnit,shipUnit,Grey)
        ikkuna = circle(shipUnit / 2, Blue)
        shipPart1 = ikkuna.against(laatikko, new Pos(laatikko.width/2,laatikko.height/2))
        shipPart2 = shipPart1.onto(bottom,new Pos(bottom.width / 2,-bottom.height / 2))
        fullShip = shipPart2.below(yla)

        // alien



      //viivan juttuja
      for (luku <- 0 until 150) { // 0 until viivojen määrä
        endPositions(luku) = newEndCoord(luku)
      }

      // Liikuttaa alusta
      //shipPos = shipPos.add(1,1)

      // r += 0.1 // ympyrän pinta-alan pienentämiseen
      middlePos = middlePos.add(math.sin(clock)*10,math.cos(clock)*10)
      theta += 0.2
    }

    //------- drawing -------//


    override def makePic(): Pic = {
      val background = rectangle(this.width, this.height,  Black)
      if (clock > 100.0) return background // After enough time, hide the animation

      var pic = background
      // Draw all the circles.
      // line(middlePos, startingPos, Red).against(pic, middlePos)
      for(x <- 0 until 150) { // 0 until viivojen määrä
        pic = line(middlePos, endPositions(x),o1.gui.Color(255-(0.4*x).toInt,x,x*2)).against(pic,middlePos)
      }
      // tuhoaa shipin
      if( shipUnit > 0.001) {
        pic = fullShip.against(pic,shipPos)
      }
      //pic = fullShip.against(pic,new Pos(250,250))
      //alien lisäys
      pic = alien.against(pic, alienPos)
      pic = asteroid.against(pic, asteroidPos)
      pic
    }


    // Effects can also receive information on mouse movements.
    // When a mouse goes to ne coordinates this method gets called.

    // We use it to draw still more circles at the mouse location

    // The lastMouseCircle trick is there to prevent accidentally drawing a lot of circles when moving the mouse very slowly
    var lastMouseCircle:Option[Circle] = None

    override def mouseAt(x: Int, y: Int) = {
      //middlePos = new Pos(x,y)
    }

    // This effect will end after enough time has passed
    def next: Boolean = clock > 110.0
  }
