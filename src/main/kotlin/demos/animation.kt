package demos

import com.anysolo.toyGraphics.*


fun main() {
    val wnd = Window(500, 200, buffered = true)

    val animation = Animation("graphicsFiles/zombie.gif", delay = 100, autoStart = true, loop = true)

    var x = wnd.width - animation.width
    val y = wnd.height - animation.height

    while(true) {
        Graphics(wnd).use { gc ->
            gc.clear()
            gc.drawAnimation(x, y, animation)
        }

        x --

        if(x < 0)
            x = wnd.width - animation.width

        sleep(10)
        AnimationManager.update()
    }
}
