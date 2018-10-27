package com.zavvytech.centerofearth.game.physics

import com.zavvytech.centerofearth.game.AnalogueController
import com.zavvytech.centerofearth.game.entities.Ship
import com.zavvytech.centerofearth.game.entities.ground.Ground
import org.jbox2d.callbacks.ContactImpulse
import org.jbox2d.callbacks.ContactListener
import org.jbox2d.collision.Manifold
import org.jbox2d.dynamics.contacts.Contact

class MiningContactListener: ContactListener {
    private val miningContactMap : MutableMap<Contact, MiningContact> = HashMap()
    private val miningContactDirectionMap: MutableMap<AnalogueController.Direction, MiningContact> = HashMap()

    override fun endContact(contact: Contact?) {
        contact?.createMiningContact()?.also {
            miningContactDirectionMap.remove(miningContactMap[contact]?.directionOfGround)
            miningContactMap.remove(contact)
        }
    }

    override fun beginContact(contact: Contact?) {
        contact?.createMiningContact()?.also {
            miningContactMap[contact] = it
            miningContactDirectionMap[it.directionOfGround] = it
        }
    }

    override fun preSolve(contact: Contact?, oldManifold: Manifold?) {}
    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {}

    private class MiningContact(private val ship: Ship, private val ground: Ground) {
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

    private fun Contact.createMiningContact(): MiningContact? {
        val aIsShip = fixtureA.body.userData is Ship
        val aIsGround = fixtureA.body.userData is Ground
        val bIsShip = fixtureB.body.userData is Ship
        val bIsGround = fixtureB.body.userData is Ground

        if (!((aIsShip && bIsGround) || (aIsGround && bIsShip))) return null

        return MiningContact(
                (if (aIsShip) fixtureA.body.userData else fixtureB.body.userData) as Ship,
                (if (aIsGround) fixtureA.body.userData else fixtureB.body.userData) as Ground
        )

    }

}
