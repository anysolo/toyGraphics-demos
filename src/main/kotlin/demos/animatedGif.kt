package demos

import com.anysolo.toyGraphics.*


fun main() {
    val wnd = Window(500, 200, buffered = true)

    val image = Image("graphicsFiles/zombie.gif")

    var x = wnd.width - image.width
    val y = wnd.height - image.height

    while(true) {
        Graphics(wnd).use { gc ->
            gc.clear()
            gc.drawImage(x, y, image)
        }

        sleep(50)

        x -= 2

        if(x < 0)
            x = wnd.width - image.width
    }
}
