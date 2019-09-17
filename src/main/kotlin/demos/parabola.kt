/* Draw a parabola function */

package demos
import com.anysolo.toyGraphics.*
import kotlin.math.roundToInt


fun main(args: Array<String>) {
    val wnd = Window(800, 800)
    val gc = Graphics(wnd)

    var x = -wnd.width/2.0

    // try to play with these coefficients
    val verticalStretch = 0.01
    val verticalOffset = 100.0

    while(x < wnd.width) {
        val y = x * x * verticalStretch + verticalOffset

        gc.drawDot(x.roundToInt() + wnd.width/2, y.roundToInt())
        x += 0.1
    }
}
