package demos.turtle

import com.anysolo.toyGraphics.*


fun main() {
  val wnd = Window(1024, 768)
  val turtle = Turtle(wnd)

  val n = 128
  val size = 8
  val angle = 360.0 / n

  repeat(n) {
    turtle.forward(size)
    turtle.turnRight(angle)
  }
}
