/* Draws a five pointed star */

package demos.turtle
import com.anysolo.toyGraphics.*

fun main() {
    val wnd = Window(800, 800)
    val turtle = Turtle(wnd)

    turtle.turnRight(90.0)

    repeat(5) {
        turtle.forward(200)
        turtle.turnRight(144.0)
    }
}
