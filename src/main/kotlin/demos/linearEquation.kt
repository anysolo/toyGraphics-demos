/* Draws a liner equation */

package demos
import com.anysolo.toyGraphics.*
import kotlin.math.roundToInt

fun main(args: Array<String>) {
    val wnd = Window(800, 800)
    val gc = Graphics(wnd)

    var x = 0.0

    // try to change a,b coefficients and see what happens
    val m = 2.0
    val b = 20.0

    while(x < wnd.width - 10) {
        // This is a mathematical linear equation
        // You can draw any straight line using this equation.
        // Coefficients m and b define the line.
        val y = m * x + b

        gc.drawDot(x.roundToInt(), y.roundToInt())
        x += 0.1
    }
}
