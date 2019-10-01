package demos

import com.anysolo.toyGraphics.*


fun main() {
    val wnd = Window(500, 200, buffered = true)

    val sprite = Sprite()

    var x = 0
    val y = wnd.height/2

    while(true) {
        Graphics(wnd).use { gc ->
            gc.clear()
            gc.drawImage(x, y, image)
        }

        sleep(50)

        x += 2

        if(x >= wnd.width)
            x = 0
    }
}
