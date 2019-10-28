package demos.zoombie

import kotlin.math.cos
import kotlin.math.sin

import com.anysolo.toyGraphics.*
import kotlin.math.PI
import kotlin.math.roundToInt
import kotlin.random.Random


var gameIsOver = false

val timeStep = 0.1
val g = 9.81

var time = 0.0
val loopSleep = 20
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
var zombieSpeed = 10.0
val zombieHeight = 140
val zombieWidth = 110
var zombieIsDying = false

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

    gc.drawLine(0, gameAreaHeight -1, gunPointX.roundToInt(), (gameAreaHeight -1 - gunPointY).roundToInt())
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

fun processKeyboard(keyboard: Keyboard, blastAnimation: Animation) {
    while (true) {
        val key = keyboard.getPressedKey() ?: break

        when(key.code) {
            KeyCodes.LEFT ->
                turnTheGun(+PI/180)

            KeyCodes.RIGHT ->
                turnTheGun(-PI/180)

            KeyCodes.SPACE ->
                if(!projectileFlying && !blastAnimation.isPlaying)
                    fireTheGun()
        }
    }
}

fun endTheGame(gc: Graphics) {
    gameIsOver = true
    gc.drawText(gameAreaWidth /3, gameAreaHeight /2, "Game is over.")
}

fun killZombie(zombieAnimation: Animation) {
    zombieDistance = gameAreaWidth.toDouble() - 1
    zombieSpeed *= 1.1

    zombieIsDying = false
    zombieAnimation.start()

    score += 10 + blastAltitude * 0.3 / (gameAreaWidth / zombieDistance)
}

fun calculateZombie() {
    if(zombieIsDying)
        return

    val probability = Random.nextDouble(500.0 / (zombieSpeed * timeStep))

    val actualSpeed = if(probability < 0.25) {
        println("Jump: $probability")
        zombieSpeed * 100
    }
    else
        zombieSpeed

    zombieDistance -= actualSpeed * timeStep
}

fun isBlastTouchedZombie() =
        zombieDistance >= projectileDistance - blastRadius &&
                zombieDistance < projectileDistance + blastRadius


fun isProjectileTouchedZombie() =
        projectileAltitude < zombieHeight &&
                projectileDistance >= zombieDistance - zombieWidth /2 &&
                projectileDistance < zombieDistance + zombieWidth /2


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

    val zombieAnimation = Animation("graphicsFiles/zombie.gif", delay = 100, autoStart = true, loop = true)
    val blastAnimation = Animation("graphicsFiles/blast.gif", delay = 25)

    turnTheGun(0.0)

    zombieDistance = (gameAreaWidth -1).toDouble()

    while(!gameIsOver) {
        processKeyboard(keyboard, blastAnimation)

        Graphics(wnd).use { gc ->
            gc.clear()

            if (projectileFlying) {
                calculateProjectile()
                drawProjectile(gc)

                if (projectileAltitude <= 0 || isProjectileTouchedZombie()) {
                    score -= 5

                    blastAnimation.start()
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

            if (blastAnimation.isPlaying) {
                gc.drawAnimation(
                        (projectileDistance - blastAnimation.width / 2).roundToInt(),
                        (gameAreaHeight - 1 - blastAltitude - blastAnimation.height / 2).roundToInt(),
                        blastAnimation
                )
            }
            else if (zombieIsDying)
                killZombie(zombieAnimation)

            if (zombieDistance != 0.0) {
                gc.drawAnimation(
                        (zombieDistance - zombieAnimation.width / 2).roundToInt(),
                        gameAreaHeight - zombieAnimation.height - 1,
                        zombieAnimation
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
        AnimationManager.update()
    }
}
