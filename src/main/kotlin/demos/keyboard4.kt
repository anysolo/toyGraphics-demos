package demos

import com.anysolo.toyGraphics.*
import com.anysolo.toyGraphics.events.EventManager
import com.anysolo.toyGraphics.events.KeyboardEvent


fun main() {
    val wnd = Window(800, 600)
    val eventManager = EventManager(wnd)

    while(true) {
        while(true) {
            val keyEvent = eventManager.takeEvent() ?: break
            if(keyEvent !is KeyboardEvent)
                continue

            println(keyEvent)

            // Some key was pressed
            if(keyEvent.isPressed) {
                if (keyEvent.code == 'Q'.toInt())
                    break

                if (keyEvent.code == KeyCodes.LEFT)
                    println("Left is pressed")

                if (keyEvent.code == KeyCodes.RIGHT)
                    println("Right is pressed")

                if (keyEvent.code == KeyCodes.ALT)
                    println("ALT is pressed")

                if (keyEvent.code == KeyCodes.SHIFT)
                    println("SHIFT is pressed")

                if (keyEvent.code == KeyCodes.CTRL)
                    println("CTRL is pressed")
            }
            else {
                if (keyEvent.code == KeyCodes.LEFT)
                    println("Left is released")

                if (keyEvent.code == KeyCodes.RIGHT)
                    println("Right is released")

                if (keyEvent.code == KeyCodes.ALT)
                    println("ALT is released")

                if (keyEvent.code == KeyCodes.SHIFT)
                    println("SHIFT is released")

                if (keyEvent.code == KeyCodes.CTRL)
                    println("CTRL is released")
            }

            println()
        }

        sleep(100)
    }
}
