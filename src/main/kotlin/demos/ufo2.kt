package demos

import com.anysolo.toyGraphics.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.roundToInt


fun main() {
    val wnd = Window(800, 600, buffered = true, background = Pal16.black)
    val keyboard = Keyboard(wnd)
    val ufo = Image("graphicsFiles/ufo-small.png")

    var x = wnd.width/2.0 - ufo.width/2
    var y = wnd.height.toDouble() - ufo.height - 10
    var speedX = 0.0
    var speedY = 0.0

    val angleStep = Math.PI/180

    val timeStep = 0.02
    var accell = 0.0
    var angle = 0.0

    while (true) {
        val g = Graphics(wnd)
        g.clear()

        while(true) {
            val key = keyboard.getPressedKey() ?: break

            when(key.code) {
                KeyCodes.LEFT ->    angle -= angleStep
                KeyCodes.RIGHT ->   angle += angleStep
                KeyCodes.UP ->      accell += 0.5
                KeyCodes.DOWN ->    accell -= 0.5
            }

        }

        val accellX = accell * sin(angle)
        val accellY = accell * cos(angle)

        speedX += accellX * timeStep
        speedY += accellY * timeStep

        x += speedX
        y += speedY

        var screenX = x
        if(screenX < 0) screenX += wnd.width
        else if (screenX > wnd.width) screenX -= wnd.width

        var screenY = wnd.height - y
        if(screenY < 0) screenY += wnd.height
        else if (screenY > wnd.height) screenY -= wnd.height

        g.drawImage(
            (screenX - ufo.width/2).roundToInt(),
            (screenY - ufo.height/2).roundToInt(),
            ufo,
            angle,
            (screenX + ufo.width/2).roundToInt(),
            (screenY + ufo.height/2).roundToInt()
        )

        g.close()
        sleep((timeStep * 1000).toInt())
    }
}
