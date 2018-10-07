package com.zavvytech.centerofearth.game

import android.graphics.Canvas
import android.graphics.RectF
import android.view.MotionEvent
import com.zavvytech.centerofearth.R
import com.zavvytech.centerofearth.graphics.BitmapSprite
import org.jbox2d.common.Vec2
import kotlin.properties.Delegates

class AnalogueController(canvasCenter: Vec2, canvasSize: Float, private val listener:Listener) {
    enum class Direction { LEFT, DOWN, RIGHT, NONE }

    private val sizeBackground = canvasSize
    private val sizeControl = canvasSize/2
    private val rectBackground = RectF(canvasCenter.x - sizeBackground/2, canvasCenter.y - sizeBackground/2,
            canvasCenter.x + sizeBackground/2, canvasCenter.y + sizeBackground/2)
    private val rectControl = RectF(canvasCenter.x - sizeControl/2, canvasCenter.y - sizeControl/2,
            canvasCenter.x + sizeControl/2, canvasCenter.y + sizeControl/2)

    private val backgroundSprite = BitmapSprite(R.drawable.control_background)
    private val controlSprite = BitmapSprite(R.drawable.control)

    var direction: Direction by Delegates.observable(Direction.NONE) {
        _, _, newValue -> listener.directionUpdated(newValue)
    }

    var released: Boolean by Delegates.observable(true) {
        _, _, newValue -> listener.releasedChanged(newValue)
    }

    interface Listener {
        fun directionUpdated(direction: Direction)
        fun releasedChanged(released: Boolean)
    }

    fun onTouch(me: MotionEvent?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun draw(canvas: Canvas) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun dispose() {
        backgroundSprite.dispose()
        controlSprite.dispose()
    }
}
