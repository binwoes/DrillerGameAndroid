package com.zavvytech.centerofearth.game.entities.ground

import android.graphics.Canvas
import android.graphics.RectF
import com.zavvytech.centerofearth.graphics.Utils
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.World
import java.util.*

object Floor {
    private val blockList = ArrayList<Ground>()
    private const val blocksAcrossScreen = 5
    private const val blockSize = Utils.screenWidthMetres / blocksAcrossScreen
    private var floorGenerationDepth = 0f
    private val randomiser = Random()

    fun draw(canvas: Canvas) {
        blockList.forEach { it.draw(canvas) }
    }

    fun generateFloorIfNeeded(viewport: RectF, world: World) {
        while (floorGenerationDepth < viewport.bottom + blockSize) {
            for (x in 0 until blocksAcrossScreen) {
                createGround(Vec2(blockSize * x, floorGenerationDepth), world)?.addToBlockList()
            }
            floorGenerationDepth += blockSize
        }
    }

    private fun createGround(worldPosition: Vec2, world: World): Ground? {
        return when (randomiser.nextFloat()) {
            in 0f..0.3f -> Dirt(blockSize, worldPosition, world)
            else -> null
        }
    }

    private fun Ground.addToBlockList() {
        blockList.add(this)
    }

}
