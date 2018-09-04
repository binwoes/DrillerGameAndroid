package com.zavvytech.centerofearth

import android.graphics.Canvas
import android.view.MotionEvent

class GameScreen : Screen {
    override val screenType = ScreenManager.ScreenType.GAME

    override fun create() {

    }

    override fun draw(canvas: Canvas) {
        canvas.drawColor((System.currentTimeMillis() % 0xFFFFFFFF).toInt())
    }

    override fun onUpdate(dt: Float) {

    }

    override fun onTouch(e: MotionEvent) {

    }

    override fun dispose() {

    }
}
