package demos.pixels

import com.anysolo.toyGraphics.*
//import com.anysolo.toyGraphics.vector.*
import kotlin.math.*


const val pixelBoardSize = 64

class Editor(width: Int, height: Int) {
    private val window = Window(width, height, buffered = true)
    private val keyboard = Keyboard(window)
    private val pixelSize = min(width, height) / pixelBoardSize
    private val board = PixelBoard(pixelBoardSize)
    private val boardWidth = pixelBoardSize * pixelSize
    private var cursorPos = Pos(0, 0)
    private var currentColorIndex = 0

    private val currentColor: Color
        get() = Pal16[currentColorIndex]

    fun execute() {
        while(true) {
            processKeyboard()

            Graphics(window).use { g ->
                g.clear()
                drawFocus(g)
                drawInfoPanel(g)
                drawBoard(g)
            }

            sleep(10)
        }
    }

    private fun drawBoard(g: Graphics) {
        board.forEach { pos: Pos, pixelColor: Color? ->
            if(pixelColor != null) {
                g.color = pixelColor
                g.drawRect(pos.x * pixelSize, pos.y * pixelSize, pixelSize, pixelSize, fill = true)
            }
        }
    }

    private fun drawFocus(g: Graphics) {
        g.color = Pal16.brightRed

        val y = cursorPos.y * pixelSize + pixelSize/2
        g.drawLine(0, y, boardWidth-1, y)

        val x = cursorPos.x * pixelSize + pixelSize/2
        g.drawLine(x, 0, x, window.height-1)
    }

    private fun nextColor(delta: Int) {
        currentColorIndex += delta

        if(currentColorIndex < 0)
            currentColorIndex = Pal16.size - currentColorIndex
        else if(currentColorIndex > Pal16.size)
            currentColorIndex = currentColorIndex - Pal16.size
    }

    private fun drawInfoPanel(g: Graphics) {
        g.color = Pal16.black
        g.drawLine(boardWidth, 0, boardWidth, window.height)

        g.color = currentColor
        g.drawRect(boardWidth + 10, 10, 50, 50, fill = true)
    }

    fun processKeyboard() {
        while (true) {
            val key = keyboard.getPressedKey() ?: break

            when(key.code) {
                KeyCodes.LEFT -> cursorPos = cursorPos.move(-1, 0)
                KeyCodes.RIGHT -> cursorPos = cursorPos.move(1, 0)
                KeyCodes.UP -> cursorPos = cursorPos.move(0, -1)
                KeyCodes.DOWN -> cursorPos = cursorPos.move(0, 1)
                'C'.toInt() -> if(key.isAlt) nextColor(-1) else nextColor(1)
                'D'.toInt() -> board[cursorPos] = currentColor
            }

            cursorPos = board.normalizePos(cursorPos)
        }
    }
}
