package demos.squash

import com.anysolo.toyGraphics.*
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin


val wnd = Window(1280, 720, buffered = true, background = Pal16.black)
val keyboard = Keyboard(wnd)

val racketHeight = 10
val racketWidth = 75

var racketX = wnd.width/2 - racketWidth/2
val racketY = wnd.height - racketHeight - 5
var racketSpeed = 0

val ballSize = 10
var ballX = 0
var ballY = 0
var ballSpeed = 1
var ballAngle = 45.0


fun processKeyboard() {
    while(true) {
        val key = keyboard.getPressedKey() ?: break

        when(key.code) {
            KeyCodes.LEFT -> racketSpeed--
            KeyCodes.RIGHT -> racketSpeed++
        }
    }
}

fun distanceFromLineToPoint(x: Int, y: Int, x1: Int, y1: Int, x2: Int, y2: Int): Int {
    val A = x - x1 // position of point rel one end of line
    val B = y - y1
    val C = x2 - x1 // vector along line
    val D = y2 - y1
    val E = -D // orthogonal vector

    val dot = A * E + B * C
    val len_sq = E * E + C * C

    return (dot.toDouble() * dot / len_sq).roundToInt();
}
fun calculateRacketMotion() {
    racketX += racketSpeed
    if(racketX < 0) {
        racketX = 0
        racketSpeed = -racketSpeed
    }

    if(racketX >= wnd.width) {
        racketX = wnd.width - racketWidth
        racketSpeed = -racketSpeed
    }
}

fun calculateBallMotion() {
    ballX += (ballSpeed * sin(ballAngle)).roundToInt()
    ballY += (ballSpeed * cos(ballAngle)).roundToInt()
}

fun drawEverything () {
    val g = Graphics(wnd)
    g.clear()

    // --------------- Drawing -------------------
    // Drawing the racket
    g.color = Pal16.blue
    g.drawRect(racketX, racketY, racketWidth, racketHeight, fill = true)

    // Drawing the ball
    g.color = Pal16.red
    g.drawOval(ballX - ballSize/2, ballY - ballSize/2, ballSize, ballSize, fill = true)

    g.close()
}

fun main() {
    // Game loop
    while(true) {
        processKeyboard()

        calculateRacketMotion()
        calculateBallMotion()

        drawEverything()
        sleep(20)
    }
}
