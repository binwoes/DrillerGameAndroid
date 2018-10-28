package com.zavvytech.centerofearth.game.mining

import com.zavvytech.centerofearth.game.AnalogueController
import com.zavvytech.centerofearth.game.entities.Ship
import com.zavvytech.centerofearth.game.entities.ground.Ground
import org.jbox2d.callbacks.ContactImpulse
import org.jbox2d.callbacks.ContactListener
import org.jbox2d.collision.Manifold
import org.jbox2d.dynamics.contacts.Contact

class MiningContactListener(val ship: Ship): ContactListener {
    private val miningContactMap : MutableMap<Contact, MiningContact> = HashMap()
    private val miningContactDirectionMap: MutableMap<AnalogueController.Direction, MiningContact> = HashMap()

    override fun beginContact(contact: Contact?) {
        contact?.createMiningContact()?.also {
            miningContactMap[contact] = it
            miningContactDirectionMap[it.directionOfGround] = it
            ship.miningDelegate.contactStarted(it.directionOfGround, it)
        }
    }

    override fun endContact(contact: Contact?) {
        contact?.createMiningContact()?.also {
            ship.miningDelegate.contactEnded(it.directionOfGround, it)
            miningContactDirectionMap.remove(miningContactMap[contact]?.directionOfGround)
            miningContactMap.remove(contact)
        }
    }

    override fun preSolve(contact: Contact?, oldManifold: Manifold?) {}
    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {}

    interface ChangeListener {
        fun contactStarted(direction: AnalogueController.Direction, miningContact: MiningContact)
        fun contactEnded(direction: AnalogueController.Direction, miningContact: MiningContact)
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
