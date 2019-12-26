package demos.pixels

import com.anysolo.toyGraphics.Graphics
import com.anysolo.toyGraphics.Keyboard
import com.anysolo.toyGraphics.Window
import com.anysolo.toyGraphics.sleep
//import com.anysolo.toyGraphics.vector.*
import kotlin.math.*


const val pixelBoardSize = 64

class Editor(width: Int, height: Int) {
    private val window = Window(width, height, buffered = true)
    private val keyboard = Keyboard(window)
    private val board = PixelBoard(pixelBoardSize, min(width, height) / pixelBoardSize)

    fun execute() {
        while(true) {
            Graphics(window).use { g ->
                board.draw(g)
            }

            sleep(10)
        }
    }
}
