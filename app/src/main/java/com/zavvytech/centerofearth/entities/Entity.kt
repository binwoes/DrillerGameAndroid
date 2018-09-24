package com.zavvytech.centerofearth.entities

import android.graphics.Canvas
import android.graphics.RectF
import android.support.annotation.DrawableRes
import com.zavvytech.centerofearth.graphics.BitmapSprite
import com.zavvytech.centerofearth.graphics.ResourceManager
import com.zavvytech.centerofearth.graphics.Utils
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.Body
import org.jbox2d.dynamics.BodyDef
import org.jbox2d.dynamics.FixtureDef
import org.jbox2d.dynamics.World

abstract class Entity(val worldPosition: Vec2, private val world: World) {
    protected abstract val bodyDef: BodyDef
    protected abstract val fixtureDef: FixtureDef
    protected abstract val width: Float
    protected abstract val height: Float
    private val body: Body by lazy {
        createBody()
    }
    protected val texture by lazy {
        ResourceManager.getTexture(textureResId())
    }
    private val sprite: BitmapSprite by lazy {
        BitmapSprite(texture)
    }
    private val canvasPosition: RectF = RectF(0f,0f,0f,0f)
        get() {
            field.left = Utils.metresToPixels(body.position.x)
            field.top = Utils.metresToPixels(body.position.y)
            field.right = field.left + Utils.metresToPixels(width)
            field.bottom = field.top + Utils.metresToPixels(height)
            return field
        }

    @DrawableRes abstract fun textureResId(): Int

    private fun createBody(): Body {
        val body: Body = world.createBody(bodyDef)
        body.createFixture(fixtureDef)
        return body
    }

    fun draw(canvas: Canvas) {
        sprite.draw(canvas, canvasPosition)
    }
}