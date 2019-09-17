package demos
import com.anysolo.toyGraphics.*

fun main() {
    val wnd = Window(800, 600)
    val gc = Graphics(wnd)

    gc.color = Pal16.red
    gc.drawRect(0, 0, 50, 50, fill = true)

    gc.color = Pal16.blue
    gc.setStrokeWidth(5)
    gc.drawLine(25, 0, 25, wnd.height-1)

    gc.color = Pal16.green
    gc.setStrokeWidth(5)
    gc.drawLine(0, 25, wnd.width-1, 25)
}
