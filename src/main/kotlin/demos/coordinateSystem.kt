package demos
import com.anysolo.toyGraphics.*

fun main() {
    val wnd = Window(800, 600)
    val g = Graphics(wnd)
    g.setStrokeWidth(5)

    g.color = Pal16.red
    g.drawRect(0, 0, 50, 50, fill = true)

    g.color = Pal16.blue
    g.drawLine(25, 0, 25, wnd.height-1)

    g.color = Pal16.green
    g.setStrokeWidth(5)
    g.drawLine(0, 25, wnd.width-1, 25)
}
