package com.zavvytech.centerofearth

import android.graphics.Canvas
import android.view.MotionEvent
import com.zavvytech.centerofearth.entities.Ship
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.Body
import org.jbox2d.dynamics.World

class GameScreen : Screen {
    override val screenType = ScreenManager.ScreenType.GAME
    private val stepIterations = 20
    private val world = World(Vec2(0f, 9.81f))
    val ship = Ship(Vec2(40f, 80f), world)

    override fun draw(canvas: Canvas) {
        canvas.drawColor((0xFFFFFFFF).toInt())
        ship.draw(canvas)
    }

    override fun onUpdate(dt: Float) {
        world.step(dt, stepIterations, stepIterations)
        world.cleanupBodies { it.position.y  < ScreenManager.viewport.top }
    }

    override fun onTouch(e: MotionEvent) {

    }

    override fun dispose() {
        world.cleanupAllBodies()
    }
}

private fun World.cleanupBodies(shouldDestroy: (body: Body) -> Boolean) {
    var curr = bodyList
    var next: Body?
    while (curr != null && shouldDestroy(curr)) {
        next = curr.next
        destroyBody(curr)
        curr = next
    }
}
private fun World.cleanupAllBodies() {
    cleanupBodies { true }
}
