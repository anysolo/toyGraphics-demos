package demos
import com.anysolo.toyGraphics.*

fun main() {
    val wnd = Window(300, 200)
    val gc = Graphics(wnd)

    val image = Image("graphicsFiles/brick-wall.jpg")
    gc.drawImage(50, 35, image)
}
