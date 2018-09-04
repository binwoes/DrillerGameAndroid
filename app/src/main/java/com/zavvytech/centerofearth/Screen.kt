package com.zavvytech.centerofearth

import android.graphics.Canvas
import android.view.MotionEvent

interface Screen {
    val screenType: ScreenManager.ScreenType
    fun create()
    fun draw(canvas: Canvas)
    fun onUpdate(dt: Float)
    fun onTouch(e: MotionEvent)
    fun dispose()
}