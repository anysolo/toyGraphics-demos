package demos.pixels

import com.anysolo.toyGraphics.Color
import com.anysolo.toyGraphics.Graphics


class PixelBoard(val boardSize: Int, val pixelSize: Int) {
    private val pixels = Array<Color?> (boardSize * boardSize) {null}

    fun getPixel(x: Int, y: Int): Color? {
        assert(x in 0 until boardSize)
        assert(y in 0 until boardSize)

        return pixels[y * boardSize + x]
    }

    fun setPixel(x: Int, y: Int, color: Color?) {
        assert(x in 0 until boardSize)
        assert(y in 0 until boardSize)

        pixels[y * boardSize + x] = color
    }

    fun draw(g: Graphics) {
        for( y in 0 until boardSize) {
            for( x in 0 until boardSize) {
                val pixelColor = getPixel(x, y)

                if(pixelColor != null) {
                    g.color = pixelColor
                    g.drawRect(x * pixelSize, y * pixelSize, pixelSize, pixelSize, fill = true)
                }
            }
        }
    }
}
