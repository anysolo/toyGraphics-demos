package demos

import com.anysolo.toyGraphics.*


fun main() {
    val wnd = Window(800, 100, buffered = true, background = Pal16.black)

    val jumpmanFrames = AnimationFrames.loadFromImageSheet("graphicsFiles/jumpman.png", 4, 3)
    val animationManager = AnimationManager()
    val jumpmanRunningLeft = Animation(jumpmanFrames, activeFrames = listOf(0, 1), delay = 100, loop = true)
    val jumpmanRunningRight = Animation(jumpmanFrames, activeFrames = listOf(4, 5), delay = 100, loop = true)
    val jumpmanJumping = Animation(jumpmanFrames, activeFrames = (8..11).toList(), delay = 100, loop = true)
    val jumpmanStanding = Animation(jumpmanFrames, activeFrames = listOf(8), delay = 100, loop = true)

    var x = wnd.width/2
    val y = wnd.height - jumpmanFrames.height-2

    var currentAnimation: Animation = jumpmanStanding
    currentAnimation.start(animationManager)

    val keyboard = Keyboard(wnd, eventMode = true)

    while(true) {
        while(true) {
            val kEvent = keyboard.getEvent() ?: break

            var newAnimation: Animation? = null

            if(kEvent.isPressed)
                when(kEvent.code) {
                    KeyCodes.LEFT -> newAnimation = jumpmanRunningLeft
                    KeyCodes.RIGHT -> newAnimation = jumpmanRunningRight
                    KeyCodes.SPACE -> newAnimation = jumpmanJumping
                }
            else
                when(kEvent.code) {
                    KeyCodes.LEFT, KeyCodes.RIGHT, KeyCodes.SPACE -> newAnimation = jumpmanStanding
                }

            if(newAnimation != null && newAnimation != currentAnimation) {
                currentAnimation.stop()
                newAnimation.start(animationManager)
                currentAnimation = newAnimation
            }
        }

        when(currentAnimation) {
            jumpmanRunningLeft -> if(x > 1) x -= 2
            jumpmanRunningRight -> if(x < wnd.width-1) x += 2
        }

        Graphics(wnd).use { gc ->
            gc.clear()

            gc.drawAnimation(x, y, currentAnimation)
        }

        sleep(20)
        animationManager.update()
    }
}
