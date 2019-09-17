package demos

import java.lang.Math.toRadians
import kotlin.math.cos
import kotlin.math.sin
import com.anysolo.toyGraphics.*
import kotlin.math.roundToInt


fun main() {
    val wnd = Window(800, 600)
    val gc = Graphics(wnd)

    val timeStep = 0.01
    val g = 9.81
    val screenScale = 200

    var t = 0.0

    val initialSpeed = 1300.0
    var distance = 0.0
    var height = 0.0

    val angle = toRadians(60.0)

    var vv = initialSpeed * sin(angle)
    val vh = initialSpeed * cos(angle)

    while(height >= 0.0) {
        vv -= g * timeStep

        distance += vh * timeStep
        height += vv * timeStep

        val x = distance / screenScale
        val y = wnd.height - height / screenScale

        gc.drawDot(x.roundToInt(), y.roundToInt())

        t += 0.1
    }
}
