package demos.python

import com.anysolo.toyGraphics.*


data class BoardXY(val x: Int, val y: Int) {
    operator fun plus(other: BoardXY) = BoardXY(x + other.x, y + other.y)
}

class Board(val size: BoardXY) {
    private val board: Array<Array<Boolean>> =
        (0 until size.y).map {
            Array(size.x) {false}
        }.toTypedArray()

    fun clear() =
        board.forEach { row -> row.fill(false) }

    operator fun get(x: Int, y: Int) = board[y][x]
    operator fun get(xy: BoardXY) = get(xy.x, xy.y)

    operator fun set(x: Int, y: Int, value: Boolean) { board[y][x] = value }
    operator fun set(xy: BoardXY, value: Boolean) {set(xy.x, xy.y, value) }

    fun forEach(lambda: (xy: BoardXY, Boolean) -> Unit) {
        board.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, blockValue ->
                lambda(BoardXY(columnIndex, rowIndex), blockValue)
            }
        }
    }

    private fun normilizeCoord(coord: Int, coordSize: Int) = when {
        coord < 0 -> coord + coordSize
        coord >= coordSize -> coord - coordSize
        else -> coord
    }

    fun normilizeXY(xy: BoardXY) = BoardXY(
        normilizeCoord(xy.x, size.x),
        normilizeCoord(xy.y, size.y)
    )
}


class PythonGame {
    private val wnd = Window(1920, 1080, background = Pal16.black, buffered = true)
    private val board = Board(BoardXY(128, 96))
    private val blockSize = Size(wnd.width/board.size.x, wnd.height/board.size.y)
    private val keyboard = Keyboard(wnd)
    private var speed = BoardXY(1, 0)
    private var headPos = BoardXY(0, board.size.y / 2)
    private var gameIsOver = false
    private var loopCounter = 0
    private var score = 0
    private var loopDelay = 100
    private val body = mutableListOf<BoardXY>()

    fun run() {
        board.clear()
        gameLoop()
    }

    private fun gameLoop() {
        while(true) {
            processKeyboard()

            if(gameIsOver)
                continue

            movePython()
            drawBoard()

            loopCounter++
            score = loopCounter

            if(loopCounter % 100 == 0 && loopDelay != 0)
                loopDelay--

            sleep(loopDelay)
        }
    }

    private fun drawBoard() {
        Graphics(wnd).use { gc ->
            gc.setFontSize(48)
            gc.clear()

            gc.color = Pal16.blue
            board.forEach { xy: BoardXY, value ->
                if(value)
                    drawBlock(gc, xy)
            }

            if(gameIsOver) {
                gc.color = Pal16.red
                gc.drawText(wnd.width / 3, wnd.height / 2, "Game Over")
            }

            gc.color = Pal16.cyan
            gc.drawText(wnd.width-100, wnd.height-50, score.toString())
        }
    }

    private fun drawBlock(gc: Graphics, xy: BoardXY) {
        gc.drawRect(
            xy.x * blockSize.width + 1,
            xy.y * blockSize.height + 1,
            blockSize.width - 2,
            blockSize.height - 2,
            fill = true
        )
    }

    private fun processKeyboard() {
        do {
            val key = keyboard.getPressedKey()

            if (key != null) {
                when(key.code) {
                    KeyCodes.LEFT -> speed = BoardXY(-1, 0)
                    KeyCodes.UP -> speed = BoardXY(0, -1)
                    KeyCodes.RIGHT -> speed = BoardXY(1, 0)
                    KeyCodes.DOWN -> speed = BoardXY(0, 1)
                }
            }
        } while(key != null)
    }

    private fun movePython() {
        headPos = board.normilizeXY(headPos + speed)

        if(board[headPos])
            finishTheGame()

        board[headPos] = true

        body.add(headPos)
        val maxLen = score / 10 + 5

        if(body.size >  maxLen) {
            board[body[0]] = false
            body.removeAt(0)
        }
    }

    private fun finishTheGame() {
        gameIsOver = true
    }

}


fun main() {
    val game = PythonGame()
    game.run()
}
