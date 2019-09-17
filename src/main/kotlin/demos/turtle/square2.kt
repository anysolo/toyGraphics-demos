// Draws a square using "repeat" loop.

package demos.turtle
import com.anysolo.toyGraphics.*


fun main() {
    val wnd = Window(800, 800)
    val turtle = Turtle(wnd)

    repeat(4) {
        turtle.forward(100)
        turtle.turnRight(90.0)
    }
}
