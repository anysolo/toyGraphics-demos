// Try how it works pressing left, right arrows, space and 'Q' keys.

package demos
import com.anysolo.toyGraphics.*

fun main() {
    val wnd = Window(1920, 1080, buffered = true)

    val rocketWidth = 50
    val rocketHeight = 10
    val y = wnd.height - 20
    var x = wnd.width/2
    var speed = 0
    var needToExit = false

    val keyboard = Keyboard(wnd)

    var bulletX = 0
    var bulletY = 0
    var bulletFlying = false
    val bulletHeight = 10
    val bulletWidth = 6
    val bulletSpeed = 3

    while(!needToExit) {
        val key = keyboard.getPressedKey()

        if(key != null) {
            when(key.code) {
                'Q'.toInt() ->
                    needToExit = true

                KeyCodes.LEFT ->
                    speed--

                KeyCodes.RIGHT ->
                    speed ++

                KeyCodes.SPACE -> {
                    bulletFlying = true
                    bulletX = x + rocketWidth/2
                    bulletY = y
                }
            }
        }

        Graphics(wnd).use { gc ->
            gc.setStrokeWidth(3)
            gc.color = Pal16.blue

            gc.clear()
            gc.drawRect(x, y, rocketWidth, rocketHeight)

            if (bulletFlying) {
                gc.drawRect(bulletX, bulletY, bulletWidth, bulletHeight)
                bulletY -= bulletSpeed

                if (bulletY < -bulletHeight)
                    bulletFlying = false
            }
        }

        x += speed

        if(x < 0)
            x = wnd.width - 1

        else if (x >= wnd.width)
            x = 0

        sleep(20)
    }

    println("The End")
    wnd.close()
}
