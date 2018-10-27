package com.zavvytech.centerofearth.game.entities

import com.zavvytech.centerofearth.R
import com.zavvytech.centerofearth.game.AnalogueController
import com.zavvytech.centerofearth.game.AnalogueController.Direction.*
import com.zavvytech.centerofearth.game.entities.ground.Ground
import com.zavvytech.centerofearth.graphics.Utils.blockSizeMetres
import org.jbox2d.callbacks.ContactImpulse
import org.jbox2d.callbacks.ContactListener
import org.jbox2d.collision.Manifold
import org.jbox2d.collision.shapes.CircleShape
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.BodyDef
import org.jbox2d.dynamics.BodyType
import org.jbox2d.dynamics.FixtureDef
import org.jbox2d.dynamics.World
import org.jbox2d.dynamics.contacts.Contact

class Ship (initialPosition: Vec2, world: World): Entity(initialPosition, world) {
    override val width: Float = blockSizeMetres * 0.8f
    override val height: Float = width * sprite.height/sprite.width
    override val bodyDef: BodyDef = createBodyDef()
    override val fixtureDef: FixtureDef = createFixtureDef()
    var travelDir: AnalogueController.Direction = NONE
    private val shipMotorStrength = body.mass*5.5f
    private val forces = mapOf(
            NONE to Vec2(),
            LEFT to Vec2(-shipMotorStrength, 0f),
            RIGHT to Vec2(shipMotorStrength, 0f),
            DOWN to Vec2(0f, shipMotorStrength)
    )
    init {
        world.setContactListener(MiningContactListener())
    }

    override fun textureResId(): Int {
        return R.drawable.ship
    }

    fun onUpdate() {
        body.applyForceToCenter(forces[travelDir])
    }

    private fun createBodyDef(initialVelocity: Vec2 = Vec2(0f,0f)): BodyDef {
        val bodyDef = BodyDef()

        bodyDef.position = initialPosition
        bodyDef.angle = 0.0f
        bodyDef.linearVelocity = initialVelocity
        bodyDef.angularVelocity = 0.0f
        bodyDef.fixedRotation = true
        bodyDef.active = true
        bodyDef.bullet = false
        bodyDef.allowSleep = true
        bodyDef.gravityScale = 1.0f
        bodyDef.linearDamping = 0.0f
        bodyDef.angularDamping = 0.0f
        bodyDef.userData = this
        bodyDef.type = BodyType.DYNAMIC
        return bodyDef
    }

    private fun createFixtureDef(): FixtureDef {
        val shape = CircleShape()
        shape.radius = width/2f
        shape.m_p.x = width/2f
        shape.m_p.y = width/2f
        val fixtureDef = FixtureDef()
        fixtureDef.shape = shape
        fixtureDef.userData = null
        fixtureDef.friction = 0.35f
        fixtureDef.restitution = 0.1f
        fixtureDef.density = 1f
        fixtureDef.isSensor = false
        return fixtureDef
    }

    private class MiningContactListener: ContactListener {
        private val miningContactMap : MutableMap<Contact, MiningContact> = HashMap()
        val miningContactDirectionMap: MutableMap<AnalogueController.Direction, MiningContact> = HashMap()

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
}