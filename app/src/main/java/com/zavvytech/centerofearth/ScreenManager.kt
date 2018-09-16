package com.zavvytech.centerofearth

import android.graphics.Canvas
import android.graphics.RectF
import android.view.MotionEvent

object ScreenManager {
    enum class ScreenType {
        SPLASH, GAME
    }

    val viewport: RectF = RectF(0f,0f,0f,0f)
    private var screens = ArrayList<Screen>()
    private var lastUpdate = System.currentTimeMillis()

    private val currentScreen: Screen?
        get() = if (screens.size >= 1) screens[screens.size - 1] else null

    fun setSize(width: Float, height: Float) {
        viewport.right = width
        viewport.bottom = height
    }

    fun setScreen(type: ScreenType): Screen {
        val newScreen = typeToScreen(type)
        screens.add(newScreen)
        return newScreen
    }

    fun finishScreen(screenToFinish: Screen) {
        screenToFinish.dispose()
        screens.remove(screenToFinish)
    }

    fun onTouch(motionEvent: MotionEvent) {
        currentScreen?.onTouch(motionEvent)
    }

    fun gameLoop(canvas: Canvas) {
        val currTime = System.currentTimeMillis()

        currentScreen?.onUpdate((currTime - lastUpdate)/1000f)
        currentScreen?.draw(canvas)

        lastUpdate = currTime
    }

    fun disposeAll() {
        while (currentScreen != null) {
            finishScreen(currentScreen!!)
        }
    }

    private fun typeToScreen(type: ScreenType): Screen {
        return when(type) {
            ScreenType.GAME -> GameScreen()
            else -> throw UnsupportedOperationException("Unsupported screen type: $type")
        }
    }
}