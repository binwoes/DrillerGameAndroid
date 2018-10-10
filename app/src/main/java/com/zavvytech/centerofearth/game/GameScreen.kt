package com.zavvytech.centerofearth.game

import android.graphics.Canvas
import android.view.MotionEvent
import com.zavvytech.centerofearth.Screen
import com.zavvytech.centerofearth.ScreenManager
import com.zavvytech.centerofearth.game.entities.Ship
import com.zavvytech.centerofearth.game.entities.Wall
import com.zavvytech.centerofearth.game.entities.ground.Floor
import com.zavvytech.centerofearth.graphics.Utils
import com.zavvytech.centerofearth.graphics.Utils.blockSizeMetres
import com.zavvytech.centerofearth.graphics.Utils.screenWidthMetres
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.Body
import org.jbox2d.dynamics.World

class GameScreen : Screen {
    override val screenType = ScreenManager.ScreenType.GAME
    private val stepIterations = 20
    private val world = World(Vec2(0f, 9.81f))
    val ship = Ship(Vec2(screenWidthMetres/2f, -blockSizeMetres*2), world)
    private val floor = Floor()
    private val walls = Wall(world)
    private val analogueStick = AnalogueController(
            Vec2(ScreenManager.viewport.width()/2, ScreenManager.viewport.height()*0.9f),
            ScreenManager.viewport.width()/3f,
            AnalogueControllerDelegate()
    )

    override fun draw(canvas: Canvas) {
        canvas.drawColor((0xFFFFFFFF).toInt())
        floor.draw(canvas)
        ship.draw(canvas)
        analogueStick.draw(canvas)
    }

    override fun onUpdate(dt: Float) {
        world.step(dt, stepIterations, stepIterations)
        ScreenManager.viewport.offsetTo(0f, Utils.metresToPixels(ship.worldPosition.y - screenWidthMetres/2))
        walls.updateWallLocations()
        floor.generateFloorIfNeeded(ScreenManager.viewport, world)
        world.cleanupBodies { it.position.y < Utils.pixelsToMetres(ScreenManager.viewport.top) }
    }

    override fun onTouch(e: MotionEvent) {
        analogueStick.onTouch(e)
    }

    override fun dispose() {
        world.cleanupAllBodies()
        analogueStick.dispose()
    }

    override fun onBackPressed() {
        ScreenManager.finishScreen(this)
    }

    class AnalogueControllerDelegate : AnalogueController.Listener {
        override fun directionUpdated(direction: AnalogueController.Direction) {
            println(direction.name)
//            TODO("NOT IMPLEMENTED")
        }

        override fun releasedChanged(released: Boolean) {
            println(if(released) "released" else "touched")
//            TODO("NOT IMPLEMENTED")
        }

    }
}

private fun World.cleanupBodies(shouldDestroy: (body: Body) -> Boolean) {
    var curr = bodyList
    var next: Body?
    while (curr != null) {
        next = curr.next
        if (shouldDestroy(curr)) {
            destroyBody(curr)
        }
        curr = next
    }
}
private fun World.cleanupAllBodies() {
    cleanupBodies { true }
}
