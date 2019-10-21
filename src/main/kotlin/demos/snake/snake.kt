package demos.snake

import com.anysolo.toyGraphics.*
import kotlin.math.roundToInt
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

    fun didItTouchApple(pos: BoardPos): BoardPos? {
        val posList = listOf(
            BoardPos(0, 0),
            BoardPos(0, -1),
            BoardPos(1, -1),
            BoardPos(1, 0),
            BoardPos(1, 1),
            BoardPos(0, 1),
            BoardPos(-1, 1),
            BoardPos(-1, 0),
            BoardPos(-1, -1)
        )

        return posList.map { normalizeXY(pos + it) }.
                map { it to get(it) }.
                firstOrNull() { it.second == BoardCell.apple } ?.first
    }
}


class SnakeGame(val amountOfApples: Int, val startingLoopDelay: Int) {
    companion object {
        const val gameAreaSize = 800
        const val infoPanWidth = 200
        const val boardSize = 60
    }

    private val wnd = Window(
        gameAreaSize + infoPanWidth, gameAreaSize,
        background = Pal16.black, buffered = true
    )

    private val board = Board(BoardPos(boardSize, boardSize))
    private val blockSize = Size(gameAreaSize/board.size.x, gameAreaSize/board.size.y)
    private val keyboard = Keyboard(wnd)
    private var speed = BoardPos(1, 0)
    private var headPos = BoardPos(0, board.size.y / 2)
    private var gameIsOver = false
    private var loopCounter = 0
    private var score = 0
    private var loopDelay = startingLoopDelay
    private val body = mutableListOf<BoardPos>()
    private var gameStartTime: Long = 0
    private var appleCount = 0

    fun run() {
        board.clear()
        createApples(amountOfApples)
        appleCount = 0
        score = 0
        gameStartTime = System.currentTimeMillis()
        gameLoop()
    }

    fun getGameTime() = System.currentTimeMillis() - gameStartTime

    private fun gameLoop() {
        while(true) {
            processKeyboard()

            if(gameIsOver)
                continue

            movePython()
            draw()

            loopCounter++
            updateScore()

            loopDelay = startingLoopDelay - score/2
            if(loopDelay < 0)
                loopDelay = 0

            sleep(loopDelay)
        }
    }

    private fun draw() {
        Graphics(wnd).use { gc ->
            gc.clear()

            board.forEach {pos: BoardPos, value -> drawBlock(gc, pos, value) }

            if(gameIsOver) {
                gc.color = Pal16.brightYellow
                gc.setFontSize(64)
                gc.drawText(wnd.width / 5, wnd.height / 2, "Game Over")
            }

            drawInfoPan(gc)
        }
    }

    private fun drawBlock(gc: Graphics, pos: BoardPos, value: BoardCell?) {
        val cellPixelPos = Point(pos.x * blockSize.width, pos.y * blockSize.height)

        when(value) {
            BoardCell.snakeCell -> {
                gc.color = if(pos == headPos) Pal16.brightYellow else Pal16.green
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
                gc.drawOval(
                    cellPixelPos.x - blockSize.width,
                    cellPixelPos.y - blockSize.width,
                    blockSize.width*2,
                    blockSize.height*2,
                    fill = true
                )
            }

            else -> {}
        }
    }

    private fun drawInfoPan(gc: Graphics) {
        gc.color = Pal16.white
        gc.drawLine(gameAreaSize, 0, gameAreaSize, wnd.height-1)

        val x = gameAreaSize + gameAreaSize/60
        val ystep = 50

        var y = 50

        gc.setFontSize(28)
        gc.color = Pal16.cyan

        gc.drawText(x, y, "Score: $score")
        y += ystep

        gc.drawText(x, y, "Apples: $appleCount")
        y += ystep

        val time = (getGameTime() / 1000.0).toDouble().format(1)
        gc.drawText(x, y, "Time: ${time} s")
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

        val applePos = board.didItTouchApple(headPos)

        when {
            board[headPos] == BoardCell.snakeCell ->
                finishTheGame()

            applePos != null -> {
                board[applePos] = null
                appleCount++
                createApples(1, headPos)
            }
        }

        board[headPos] = BoardCell.snakeCell

        body.add(headPos)
        val maxLen = 5 + appleCount

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

                if(board[boardPos] == BoardCell.snakeCell)
                    continue

                board[boardPos] = BoardCell.apple
                break
            }
        }
    }

    private fun updateScore() {
        if(getGameTime() > 0.0)
            score = appleCount + (appleCount.toDouble() / (getGameTime()/10000.0)).roundToInt()
    }
}


fun main() {
    val game = SnakeGame(amountOfApples = 3, startingLoopDelay = 150)
    game.run()
}
