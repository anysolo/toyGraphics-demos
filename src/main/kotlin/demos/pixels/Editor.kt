package demos.pixels

import com.anysolo.toyGraphics.*
//import com.anysolo.toyGraphics.vector.*
import kotlin.math.*


const val pixelBoardSize = 64

class Editor(width: Int, height: Int) {
    private val window = Window(width, height, buffered = true)
    private val keyboard = Keyboard(window)
    private val board = PixelBoard(pixelBoardSize, min(width, height) / pixelBoardSize)

    fun execute() {
        board.setPixel(10, 10, Pal16.blue)

        while(true) {
            Graphics(window).use { g ->
                board.draw(g)
            }

            sleep(10)
        }
    }
}
