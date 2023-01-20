package s1.demo
import java.awt.image.BufferedImage
import o1.gui.Pic

/**
 * This is the base class for every demo effect you implement.
 * 
 * @constructor Creates a new demo effect. Effects are shown in the [[DemoArea]].
 * @param width width of the effect in pixels
 * @param height height of the image in pixels
 */
abstract class Effect(val width: Int, val height: Int) {
      
  /**
   * Changes the state of the effect. Similar to the tick method in Flappy
   */
  def changeThings(): Unit
  
  /**
   * Creates an os.gui.Pic for the current state of the effect
   */
  
  def makePic(): Pic
 
  /**
   * The effects can be manipulated with mouse if desired. This method is called whenever
   * mouse is hovered over the effect.
   * 
   * By default the method does nothing
   * 
   * @param x the x-location of the mouse
   * @param y the y-location of the mouse
   */
  def mouseAt(x: Int, y: Int) = {}
  
  /**
   * This method tells the Demo engine when it is time to switch to the next effect
   * If this is the only effect, the method always returns '''false'''
   * 
   * @return true when this effect is over, false otherwise 
   */
  def next: Boolean
}