package demos

import com.anysolo.toyGraphics.*


fun main() {
    val wnd = Window(800, 600, buffered = true)
    val keyboard = Keyboard(wnd)

    var x = wnd.width/2
    var y = wnd.height/2
    var step = 1

    var running = true
    var clearScreen = true

    while(running) {
        do {
            val key = keyboard.getPressedKey()

            if (key != null) {
                if(key.code == KeyCodes.ESCAPE)
                    running = false

                when(key.code) {
                    'Z'.toInt() -> x -= step
                    'X'.toInt() -> x += step
                    'N'.toInt() -> y -= step
                    'M'.toInt() -> y += step
                    'K'.toInt() -> step--
                    'L'.toInt() -> step++

                    KeyCodes.SPACE -> clearScreen = !clearScreen
                }

                println("x=$x, y=$y, step: $step, clearScreen: $clearScreen")
            }

        } while(key != null)

        val g = Graphics(wnd)
        if(clearScreen)
            g.clear()

        g.drawRect(x, y, 10, 10)
        g.close()

        sleep(10)
    }
}
