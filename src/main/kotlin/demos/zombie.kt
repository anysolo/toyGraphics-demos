// This game is deliberately written using minimal subset of Kotlin language
// to be easily understandable for a beginner programmer.

package demos

import kotlin.math.*
import kotlin.random.Random
import com.anysolo.toyGraphics.*

var gameIsOver = false

val timeStep = 0.01
val g = 9.81

var time = 0.0
val loopSleep = 2
val gunLength = 50
var gunAngle = PI / 4
var gunPointX = 0.0
var gunPointY = 0.0


val projectileRadius = 10
val projectileInitialSpeed = 125.0
var projectileDistance = 0.0
var projectileAltitude = 0.0
var projectileVerticalSpeed = 0.0
var projectileHorizontalSpeed = 0.0

var projectileFlying = false

var gameAreaHeight = 0
var gameAreaWidth = 0

var zombieDistance = 0.0
var zombieSpeed = 0.1
val zombieHeight = 140
val zombieWidth = 110
var zombieIsDying = false

var blastCount = 0
val blastRadius = 30.0
var blastAltitude = 0.0

var score = 0.0


fun drawProjectile(gc: Graphics) {
    val x = projectileDistance
    val y = gc.window.height - projectileAltitude

    gc.color = Pal16.red
    gc.drawOval(x.roundToInt(), y.roundToInt(), projectileRadius, projectileRadius, fill = true)
}

fun calculateProjectile() {
    projectileVerticalSpeed -= g * timeStep

    projectileDistance += projectileHorizontalSpeed * timeStep
    projectileAltitude += projectileVerticalSpeed * timeStep
}

fun drawGun(gc: Graphics) {
    gc.color = Pal16.blue
    gc.setStrokeWidth(10)

    gc.drawLine(0, gameAreaHeight-1, gunPointX.roundToInt(), (gameAreaHeight-1 - gunPointY).roundToInt())
}

fun turnTheGun(degreeDelta: Double) {
    gunAngle += degreeDelta

    val minimumAngle = PI / 8

    if(gunAngle < minimumAngle)
        gunAngle = minimumAngle

    gunPointX = gunLength * cos(gunAngle)
    gunPointY = gunLength * sin(gunAngle)
}

fun fireTheGun() {
    projectileFlying = true

    projectileVerticalSpeed = projectileInitialSpeed * sin(gunAngle)
    projectileHorizontalSpeed = projectileInitialSpeed * cos(gunAngle)

    projectileAltitude = gunPointY
    projectileDistance = gunPointX
}

fun processKeyboard(keyboard: Keyboard) {
    while (true) {
        val key = keyboard.getPressedKey() ?: break

        when(key.code) {
            KeyCodes.LEFT ->
                turnTheGun(+PI/180)

            KeyCodes.RIGHT ->
                turnTheGun(-PI/180)

            KeyCodes.SPACE ->
                if(!projectileFlying && blastCount == 0)
                    fireTheGun()
        }
    }
}

fun endTheGame(gc: Graphics) {
    gameIsOver = true
    gc.drawText(gameAreaWidth/3, gameAreaHeight/2, "Game is over.")
}

fun killZombie() {
    zombieDistance = gameAreaWidth.toDouble() - 1
    zombieSpeed += 0.04
    zombieIsDying = false

    score += 10 + blastAltitude * 0.3 / (gameAreaWidth / zombieDistance)
}

fun calculateZombie() {
    if(zombieIsDying)
        return

    zombieDistance -= if(Random.nextDouble(500.0 / zombieSpeed) < 0.1)
        zombieSpeed * 500
    else
        zombieSpeed
}

fun isBlastTouchedZombie() =
    zombieDistance >= projectileDistance - blastRadius &&
    zombieDistance < projectileDistance + blastRadius


fun isProjectileTouchedZombie() =
    projectileAltitude < zombieHeight &&
    projectileDistance >= zombieDistance - zombieWidth/2 &&
    projectileDistance < zombieDistance + zombieWidth/2


fun drawScore(gc: Graphics) {
    gc.setFontSize(30)
    gc.color = Pal16.green
    gc.drawText(10, 40, "Score: " + score.format(2))
}

fun main() {
    val wnd = Window(1920, 1080, background = Pal16.black, buffered = true)

    gameAreaHeight = wnd.height
    gameAreaWidth = wnd.width

    val keyboard = Keyboard(wnd)

    val zombieImage = Image("graphicsFiles/zombie.gif")
    val blastImage = Image("graphicsFiles/blast.gif")

    turnTheGun(0.0)

    zombieDistance = (gameAreaWidth-1).toDouble()

    while(!gameIsOver) {
        processKeyboard(keyboard)

        Graphics(wnd).use { gc ->
            gc.clear()

            if (projectileFlying) {
                calculateProjectile()
                drawProjectile(gc)

                if (projectileAltitude <= 0 || isProjectileTouchedZombie()) {
                    score -= 5

                    blastCount = 435
                    projectileFlying = false

                    if (isProjectileTouchedZombie()) {
                        blastAltitude = projectileAltitude
                        zombieIsDying = true
                    } else {
                        if (isBlastTouchedZombie())
                            zombieIsDying = true

                        blastAltitude = 0.0
                    }
                }
            }

            drawScore(gc)
            drawGun(gc)

            if (blastCount != 0) {
                gc.drawImage(
                        (projectileDistance - blastImage.width / 2).roundToInt(),
                        (gameAreaHeight - 1 - blastAltitude - blastImage.height / 2).roundToInt(),
                        blastImage
                )

                blastCount--

                if (blastCount == 0 && zombieIsDying)
                    killZombie()
            }

            if (zombieDistance != 0.0) {
                gc.drawImage(
                        (zombieDistance - zombieImage.width / 2).roundToInt(),
                        gameAreaHeight - zombieImage.height - 1,
                        zombieImage
                )

                if (!zombieIsDying) {
                    calculateZombie()

                    if (zombieDistance <= 0.0)
                        endTheGame(gc)
                }
            }
        }

        time += timeStep
        score += timeStep / 100

        sleep(loopSleep)
    }
}
