package s1.demo.rendering

import o1._
import o1.gui.Pic

// Object represents a 3D model with the given vertices and indices (and a coordinate transform function)
class Object(vertices: Vector[Vertex], indices: Vector[Index], transform: CoordTransform) {
  private var projectedVertices = Vector.fill(vertices.length)(Vertex.newBlanket())

  // Re-project all the object's vertices using the given projection matrix
  def updateProjection(projection: Matrix4): Unit = {
    projectedVertices = vertices.map(vertex => {
      val transformed = projection * vertex             // First project the vertex using the projection
      Vertex(transformed / transformed.w, vertex.color) // Then divide by the w-component, which stores the perspective transform depth information
    })
  }

  // Render all the object's projected vertices using o1.circle()
  def renderVertices(base: Pic): Pic = {
    projectedVertices.foldLeft(base)((pic, vertex) =>
      pic.place(circle(20, vertex.color), transform(vertex.x, vertex.y)))
  }

  // Render all the indices between the object's projected vertices using o1.line()
  def renderIndices(base: Pic): Pic = {
    indices.foldLeft(base)((pic, index) => {
      val (start, end) = (projectedVertices(index.a), projectedVertices(index.b))
      pic.place(line(transform(start.x, start.y),
                     transform(end.x, end.y), index.color),
                     transform(start.x, start.y))
    })
  }

  // Convenience methods to change the color of all vertices/indices
  def setVertexColor(color: Color): Unit = vertices.foreach(_.color = color)
  def setIndexColor(color: Color): Unit = indices.foreach(_.color = color)
}