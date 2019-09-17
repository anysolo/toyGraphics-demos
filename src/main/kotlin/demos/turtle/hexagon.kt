package demos.turtle
import com.anysolo.toyGraphics.*


fun main() {
    val wnd = Window(800, 800)
    val turtle = Turtle(wnd)

    // write code drawing a hexagon with sides 100 pixels sides
    repeat(6) {
        turtle.forward(100)
        turtle.turnRight(60.0)
    }
}
