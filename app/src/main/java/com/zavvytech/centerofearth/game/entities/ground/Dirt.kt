package com.zavvytech.centerofearth.game.entities.ground

import com.zavvytech.centerofearth.R
import com.zavvytech.centerofearth.game.entities.Ship
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.World

class Dirt(blockSize: Float, worldPosition: Vec2, world: World) : Ground(blockSize, worldPosition, world) {
    override fun onMine() {

    }

    override fun groundTypeProbability(depthMetres: Float): Float {
        return 1f
    }

    override fun miningTimeMillis(ship: Ship): Long {
        return 3000
    }

    override fun textureResId(): Int {
        return R.drawable.dirt
    }
}