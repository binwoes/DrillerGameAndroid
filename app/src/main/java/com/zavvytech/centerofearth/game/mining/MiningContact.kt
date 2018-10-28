package com.zavvytech.centerofearth.game.mining

import com.zavvytech.centerofearth.game.AnalogueController
import com.zavvytech.centerofearth.game.entities.Ship
import com.zavvytech.centerofearth.game.entities.ground.Ground

class MiningContact(val ship: Ship, val ground: Ground) {
    val directionOfGround by lazy {
        fun computeDir(): AnalogueController.Direction {
            val relVec = ship.worldCentre.sub(ground.worldCentre)
            val leftOrUp = (-relVec.y >= relVec.x)
            val rightOrUp = (relVec.x >= relVec.y)
            if (rightOrUp) {
                if (leftOrUp) {
                    return AnalogueController.Direction.NONE
                }
                return AnalogueController.Direction.RIGHT
            } else {
                if (leftOrUp) {
                    return AnalogueController.Direction.LEFT
                }
                return AnalogueController.Direction.DOWN
            }
        }
        computeDir()
    }
}