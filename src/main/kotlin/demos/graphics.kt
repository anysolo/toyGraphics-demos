package demos
import com.anysolo.toyGraphics.*

fun main() {
    val wnd = Window(500, 500)
    val gc = Graphics(wnd)

    gc.drawRect(50, 50, 100, 50)

    gc.color = Pal16.red
    gc.setStrokeWidth(2)
    gc.drawLine(50, 350, 400, 450)

    gc.color = Pal16.blue
    gc.setStrokeWidth(4)
    gc.drawOval(350, 300, 100, 50)

    gc.color = Pal16.green
    gc.setFontSize(32)
    gc.drawText(150, 200, "Some text")
}
