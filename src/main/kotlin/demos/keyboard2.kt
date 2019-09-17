package demos

import com.anysolo.toyGraphics.*


fun main() {
    val wnd = Window(800, 600)
    val keyboard = Keyboard(wnd)

    while(true) {
        do {
            val key = keyboard.getPressedKey()

            if (key != null) {
                println(key)
                println("key: " + key.code)

                if(key.code == KeyCodes.ESCAPE)
                    println("Escape key. Does not matter shift or not shift")

                if(key.code == KeyCodes.ESCAPE && key.isShift)
                    println("Escape key + shift")

                if(key.code == KeyCodes.ESCAPE && !key.isShift)
                    println("Escape key. No shift")

                // An empty line before output from the next loop iteration
                println()
            }

        } while(key != null)

        sleep(1)
    }
}
