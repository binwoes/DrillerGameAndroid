package com.zavvytech.centerofearth.game

import android.graphics.Canvas
import android.view.MotionEvent
import com.zavvytech.centerofearth.Screen
import com.zavvytech.centerofearth.ScreenManager
import com.zavvytech.centerofearth.game.entities.Ship
import com.zavvytech.centerofearth.game.entities.ground.Dirt
import com.zavvytech.centerofearth.graphics.Utils.screenWidthMetres
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.Body
import org.jbox2d.dynamics.World

class GameScreen : Screen {
    override val screenType = ScreenManager.ScreenType.GAME
    private val stepIterations = 20
    private val world = World(Vec2(0f, 9.81f))
    val ship = Ship(Vec2(screenWidthMetres * 2/5, 0f), world)
    val floor = listOf(Dirt(Vec2(screenWidthMetres*2/5, screenWidthMetres), world))

    override fun draw(canvas: Canvas) {
        canvas.drawColor((0xFFFFFFFF).toInt())
        ship.draw(canvas)
        floor.forEach { it.draw(canvas) }
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

    override fun onBackPressed() {
        ScreenManager.finishScreen(this)
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
