package demos

import com.anysolo.toyGraphics.*
import kotlin.math.*


fun main() {
    // If you turn autoSync off and call wnd.sync() only when needed,
    // the drawing becomes much faster.
    // Change this to false to see the difference in speed.
    val useAutoSync = true

    val wnd = Window(1920, 1080, autoSync = useAutoSync)
    val gc = Graphics(wnd)

    var x = 0.0
    var y = 0.0

    val scaleX = 0.25
    val scaleY = 0.5

    var colorIndex = 0
    var offsetX = 0.0

    gc.color = Pal16[colorIndex]

    while(true) {
        x += 0.001
        y = cos(x * PI/2 + offsetX)

        val screenX = x * wnd.width * scaleX / 2
        val screenY = y * wnd.height * scaleY / 2 + wnd.height/2

        if(screenX >= wnd.width) {
            x = 0.0
            offsetX += 0.2

            colorIndex = if(colorIndex < 15) colorIndex + 1 else 0
            gc.color = Pal16[colorIndex]

            if(!useAutoSync)
                wnd.sync()
        }
        else
            gc.drawDot(screenX.roundToInt(), screenY.roundToInt())
    }
}
