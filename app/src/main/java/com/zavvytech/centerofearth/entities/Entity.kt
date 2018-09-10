package com.zavvytech.centerofearth.entities

import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.Body
import org.jbox2d.dynamics.BodyDef
import org.jbox2d.dynamics.FixtureDef
import org.jbox2d.dynamics.World

abstract class Entity(var position: Vec2, val world: World) {
    abstract var bodyDef: BodyDef
    abstract var fixtureDef: FixtureDef
    val body: Body by lazy {
        createBody()
    }

    private fun createBody(): Body {
        val body: Body = world.createBody(bodyDef)
        body.createFixture(fixtureDef)
        return body
    }
}