/* Flying a square using buffered window */

package demos
import com.anysolo.toyGraphics.*

fun main() {
    val wnd = Window(1920, 1080, buffered = true)

    val objectSize = 50
    val y = wnd.height / 2 - objectSize/2
    var x = 0

    while(true) {
        Graphics(wnd).use {gc ->
            gc.clear()
            gc.setStrokeWidth(6)
            gc.drawRect(x, y, objectSize, objectSize)
        }

        x = if(x < wnd.width) x + 1 else 0
        sleep(5)
    }
}
