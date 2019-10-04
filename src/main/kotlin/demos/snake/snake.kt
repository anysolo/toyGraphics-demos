package demos.snake

import com.anysolo.toyGraphics.*
import kotlin.random.Random


data class BoardPos(val x: Int, val y: Int) {
    operator fun plus(other: BoardPos) = BoardPos(x + other.x, y + other.y)
}

enum class BoardCell {
    snakeCell,
    apple
}

class Board(val size: BoardPos) {
    private val board: Array<Array<BoardCell?>> =
        (0 until size.y).map {
            Array<BoardCell?>(size.x) {null}
        }.toTypedArray()

    fun clear() =
        board.forEach { row -> row.fill(null) }

    operator fun get(x: Int, y: Int): BoardCell? = board[y][x]
    operator fun get(xy: BoardPos): BoardCell? = get(xy.x, xy.y)

    operator fun set(x: Int, y: Int, value: BoardCell?)  { board[y][x] = value }
    operator fun set(xy: BoardPos, value: BoardCell?)    {set(xy.x, xy.y, value) }

    fun forEach(lambda: (xy: BoardPos, cellValue: BoardCell?) -> Unit) {
        board.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, cellValue ->
                lambda(BoardPos(columnIndex, rowIndex), cellValue)
            }
        }
    }

    private fun normalizeCoord(coord: Int, coordSize: Int) = when {
        coord < 0 -> coord + coordSize
        coord >= coordSize -> coord - coordSize
        else -> coord
    }

    fun normalizeXY(xy: BoardPos) = BoardPos(
        normalizeCoord(xy.x, size.x),
        normalizeCoord(xy.y, size.y)
    )

    fun randomPos() = BoardPos(
        Random.nextInt(0, size.x-1),
        Random.nextInt(0, size.y-1)
    )
}


class SnakeGame(val amountOfApples: Int, val startingLoopDelay: Int) {
    private val wnd = Window(800, 800, background = Pal16.black, buffered = true)
    private val board = Board(BoardPos(60, 60))
    private val blockSize = Size(wnd.width/board.size.x, wnd.height/board.size.y)
    private val keyboard = Keyboard(wnd)
    private var speed = BoardPos(1, 0)
    private var headPos = BoardPos(0, board.size.y / 2)
    private var gameIsOver = false
    private var loopCounter = 0
    private var score = 0
    private var loopDelay = startingLoopDelay
    private val body = mutableListOf<BoardPos>()

    fun run() {
        board.clear()
        createApples(amountOfApples)
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

            if(score < startingLoopDelay)
                loopDelay = startingLoopDelay - score

            sleep(loopDelay)
        }
    }

    private fun drawBoard() {
        Graphics(wnd).use { gc ->
            gc.setFontSize(48)
            gc.clear()

            board.forEach {pos: BoardPos, value -> drawBlock(gc, pos, value) }

            if(gameIsOver) {
                gc.color = Pal16.red
                gc.drawText(wnd.width / 3, wnd.height / 2, "Game Over")
            }

            gc.color = Pal16.cyan
            gc.drawText(wnd.width-100, wnd.height-50, score.toString())
        }
    }

    private fun drawBlock(gc: Graphics, pos: BoardPos, value: BoardCell?) {
        val cellPixelPos = Point(pos.x * blockSize.width, pos.y * blockSize.height)

        when(value) {
            BoardCell.snakeCell -> {
                gc.color = Pal16.blue
                gc.drawRect(
                        cellPixelPos.x + 1,
                        cellPixelPos.y + 1,
                        blockSize.width - 2,
                        blockSize.height - 2,
                        fill = true
                )
            }

            BoardCell.apple -> {
                gc.color = Pal16.red
                gc.drawOval(cellPixelPos.x, cellPixelPos.y, blockSize.width, blockSize.height, fill = true)
            }

            else -> {}
        }
    }

    private fun processKeyboard() {
        do {
            val key = keyboard.getPressedKey()

            if (key != null) {
                val newSpeed = when(key.code) {
                    KeyCodes.LEFT -> BoardPos(-1, 0)
                    KeyCodes.UP -> BoardPos(0, -1)
                    KeyCodes.RIGHT -> BoardPos(1, 0)
                    KeyCodes.DOWN -> BoardPos(0, 1)
                    else -> null
                }

                if(newSpeed != null) {
                    if(newSpeed.x != -speed.x && newSpeed.y != -speed.y)
                        speed = newSpeed
                }
            }
        } while(key != null)
    }

    private fun movePython() {
        headPos = board.normalizeXY(headPos + speed)

        when(board[headPos]) {
            BoardCell.snakeCell ->
                finishTheGame()

            BoardCell.apple -> {
                score++
                createApples(1, headPos)
            }

            null -> {}
        }

        board[headPos] = BoardCell.snakeCell

        body.add(headPos)
        val maxLen = 5 + score

        if(body.size >  maxLen) {
            board[body[0]] = null
            body.removeAt(0)
        }
    }

    private fun finishTheGame() {
        gameIsOver = true
    }

    private fun createApples(amount: Int, excludePos: BoardPos? = null) {
        repeat(amount) {
            while(true) {
                val boardPos = board.randomPos()
                if(excludePos != null && boardPos == excludePos)
                    continue

                board[boardPos] = BoardCell.apple
                break
            }
        }
    }
}


fun main() {
    val game = SnakeGame(amountOfApples = 3, startingLoopDelay = 100)
    game.run()
}
