package com.zavvytech.centerofearth.game.entities

import com.zavvytech.centerofearth.ScreenManager
import com.zavvytech.centerofearth.graphics.Utils.screenWidthMetres
import org.jbox2d.collision.shapes.EdgeShape
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.BodyDef
import org.jbox2d.dynamics.BodyType
import org.jbox2d.dynamics.World

class Wall(world: World) {
    private val leftWall: Line = createLeftWall(world)
    private val rightWall: Line = createRightWall(world)

    init {
        updateWallLocations()
    }

    private fun createRightWall(world: World): Line {
        return createWall(world, screenWidthMetres)
    }

    private fun createLeftWall(world: World): Line {
        return createWall(world, 0f)
    }

    private fun createWall(world: World, xPos: Float): Line {
        val bodyDef = BodyDef()
        bodyDef.type = BodyType.STATIC
        val edge = Line(Vec2(xPos, 0f), Vec2(xPos, 0f))
        val shape = EdgeShape()
        shape.set(edge.start, edge.end)
        world.createBody(bodyDef).createFixture(shape, 0f)
        return edge
    }

    fun updateWallLocations() {
        leftWall.start.y = ScreenManager.viewport.top - ScreenManager.viewport.height()
        leftWall.end.y = ScreenManager.viewport.bottom + ScreenManager.viewport.height()
        rightWall.start.y = ScreenManager.viewport.top - ScreenManager.viewport.height()
        rightWall.end.y = ScreenManager.viewport.bottom + ScreenManager.viewport.height()
    }

    private data class Line(val start: Vec2 = Vec2(), val end: Vec2 = Vec2())
}
