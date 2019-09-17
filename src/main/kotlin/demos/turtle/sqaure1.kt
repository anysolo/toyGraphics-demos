// Draws a square. For absolute beginners.

package demos.turtle
import com.anysolo.toyGraphics.*

fun main() {
    val wnd = Window(800, 600)
    val turtle = Turtle(wnd)

    turtle.forward(100)
    turtle.turnRight(90.0)

    turtle.forward(100)
    turtle.turnRight(90.0)

    turtle.forward(100)
    turtle.turnRight(90.0)

    turtle.forward(100)
    turtle.turnRight(90.0)
}
