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

                if(key.code == 'Q'.toInt())
                    println("'Q' key. Does not matter Alt or not Alt")

                if(key.code == 'Q'.toInt() && key.isAlt)
                    println("'Q' key + Alt")

                if(key.code == 'Q'.toInt() && !key.isAlt)
                    println("'Q' key. No Alt")

                // An empty line before output from the next loop iteration
                println()
            }

        } while(key != null)

        sleep(1)
    }
}
