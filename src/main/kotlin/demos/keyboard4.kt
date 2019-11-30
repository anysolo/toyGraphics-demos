package demos

import com.anysolo.toyGraphics.*


fun main() {
    val wnd = Window(800, 600)
    val keyboard = Keyboard(wnd, eventMode=true)

    while(true) {
        do {
            val keyEvent = keyboard.getEvent()

            if (keyEvent != null) {
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

        } while(keyEvent != null)

        sleep(100)
    }
}
