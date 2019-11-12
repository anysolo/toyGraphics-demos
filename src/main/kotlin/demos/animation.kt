package demos

import com.anysolo.toyGraphics.*


fun main() {
    val wnd = Window(500, 200, buffered = true)

    val animationManager = AnimationManager()
    val animationFrames = AnimationFrames.loadFromAnimatedGif("graphicsFiles/zombie.gif")
    val animation = Animation(animationFrames, delay = 100, loop = true)
    animation.start(animationManager)

    var x = wnd.width - animation.width
    val y = wnd.height - animation.height

    while(true) {
        Graphics(wnd).use { gc ->
            gc.clear()
            gc.drawAnimation(x, y, animation)
        }

        x --

        sleep(20)
        animationManager.update()
    }
}
