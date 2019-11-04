package demos

import com.anysolo.toyGraphics.*
import kotlin.math.roundToInt


fun main() {
    val wnd = Window(800, 600, buffered = true, background = Pal16.black)
    val keyboard = Keyboard(wnd)
    val ufo = Image("graphicsFiles/ufo-small.png")

    var x = wnd.width/2.0 - ufo.width/2
    var y = wnd.height.toDouble() - ufo.height - 10
    var speedX = 0.0
    var speedY = 0.0

    val speedStepX = 1.0
    val speedStepY = 1.0

    while (true) {
        val g = Graphics(wnd)
        g.clear()

        while(true) {
            val key = keyboard.getPressedKey() ?: break

            when(key.code) {
                KeyCodes.LEFT ->    speedX -= speedStepX
                KeyCodes.RIGHT ->   speedX += speedStepX
                KeyCodes.UP ->      speedY -= speedStepY
                KeyCodes.DOWN ->    speedY += speedStepY
            }
        }

        x += speedX
        y += speedY

        g.drawImage(x.roundToInt(), y.roundToInt(), ufo)

        g.close()
        sleep(20)
    }
}
