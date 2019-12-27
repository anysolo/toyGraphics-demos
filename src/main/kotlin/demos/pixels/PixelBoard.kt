package demos.pixels

import com.anysolo.toyGraphics.Color
import com.anysolo.toyGraphics.Graphics
import demos.snake.BoardPos


data class Pos(val x: Int, val y: Int) {
    fun move(x: Int, y: Int) = Pos(this.x + x, this.y + y)
}


class PixelBoard(val boardSize: Int) {
    private val pixels = Array<Color?> (boardSize * boardSize) {null}

    operator fun get(pos: Pos): Color? {
        assert(pos.x in 0 until boardSize)
        assert(pos.y in 0 until boardSize)

        return pixels[pos.y * boardSize + pos.x]
    }

    operator fun set(pos: Pos, color: Color?) {
        assert(pos.x in 0 until boardSize)
        assert(pos.y in 0 until boardSize)

        pixels[pos.y * boardSize + pos.x] = color
    }

    fun clear() = pixels.fill(null)

    fun forEach(block: (pos: Pos, color: Color?) -> Unit) {
        for (y in 0 until boardSize) {
            for (x in 0 until boardSize) {
                val pos = Pos(x, y)
                block(pos, get(pos))
            }
        }
    }

    private fun normalizeCoord(coord: Int, coordSize: Int) = when {
        coord < 0 -> coord + coordSize
        coord >= coordSize -> coord - coordSize
        else -> coord
    }

    fun normalizePos(pos: Pos) = Pos(
        normalizeCoord(pos.x, boardSize),
        normalizeCoord(pos.y, boardSize)
    )
}
