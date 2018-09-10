package com.zavvytech.centerofearth.entities

import org.jbox2d.collision.shapes.CircleShape
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.BodyDef
import org.jbox2d.dynamics.BodyType
import org.jbox2d.dynamics.FixtureDef
import org.jbox2d.dynamics.World


class Ship (position: Vec2, world: World): Entity(position, world) {
    override var bodyDef: BodyDef = createBodyDef()
    override var fixtureDef: FixtureDef = createFixtureDef()

    fun createBodyDef(initialVelocity: Vec2 = Vec2(0f,0f)): BodyDef {
        val bodyDef = BodyDef()

        bodyDef.position = position
        bodyDef.angle = 0.0f
        bodyDef.linearVelocity = initialVelocity
        bodyDef.angularVelocity = 0.0f
        bodyDef.fixedRotation = false
        bodyDef.active = true
        bodyDef.bullet = false
        bodyDef.allowSleep = true
        bodyDef.gravityScale = 1.0f
        bodyDef.linearDamping = 0.0f
        bodyDef.angularDamping = 0.0f
        bodyDef.userData = ObjectType.SHIP
        bodyDef.type = BodyType.DYNAMIC
        return bodyDef
    }

    fun createFixtureDef(): FixtureDef {
        val shape = CircleShape()
        shape.radius = 7.5f
        val fixtureDef = FixtureDef()
        fixtureDef.shape = shape
        fixtureDef.userData = null
        fixtureDef.friction = 0.35f
        fixtureDef.restitution = 0.05f
        fixtureDef.density = 1f
        fixtureDef.isSensor = false
        return fixtureDef
    }
}