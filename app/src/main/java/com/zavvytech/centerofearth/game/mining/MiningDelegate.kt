package com.zavvytech.centerofearth.game.mining

import android.os.Handler
import com.zavvytech.centerofearth.game.AnalogueController
import com.zavvytech.centerofearth.game.entities.Ship
import com.zavvytech.centerofearth.game.entities.ground.Ground

class MiningDelegate: MiningContactListener.ChangeListener, AnalogueController.Listener {
    private var handler = Handler()
    private var groundMining: MutableMap<Ground, () -> Unit> = HashMap()

    override fun contactStarted(direction: AnalogueController.Direction, miningContact: MiningContact) {
        if (miningContact.ship.travelDir == direction &&
                miningContact.ship.travelDir != AnalogueController.Direction.NONE) {
            addMiningCallback(miningContact.ground, miningContact.ship)
        }
    }

    override fun contactEnded(direction: AnalogueController.Direction, miningContact: MiningContact) {
        removeMiningCallback(miningContact.ground)
    }

    override fun directionUpdated(direction: AnalogueController.Direction) {

    }

    override fun releasedChanged(released: Boolean) {}

    private fun addMiningCallback(ground: Ground, ship: Ship) {
        groundMining[ground] = { ground.onMine() }
        handler.postDelayed(groundMining[ground], ground.miningTimeMillis(ship))
    }

    private fun removeMiningCallback(ground: Ground) {
        handler.removeCallbacks(groundMining[ground])
        groundMining.remove(ground)
    }

}
