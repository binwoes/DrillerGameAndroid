package com.zavvytech.centerofearth

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.SurfaceHolder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SurfaceHolder.Callback {
    private var isRunning = false
        set(value) {
            field = value
            if (value) startGameRunning()
            else stopGameRunning()
        }

    private var gameThread: Thread? = null
    private val endGameTimeout: Long = 3000
    private var surfaceCreated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ScreenManager.setScreen(ScreenManager.ScreenType.GAME)
        isRunning = true
        surfaceView.holder.addCallback(this)
        surfaceView.setOnTouchListener { _, motionEvent ->
            ScreenManager.onTouch(motionEvent)
            true
        }
    }

    override fun onDestroy() {
        isRunning = false
        super.onDestroy()
    }

    private fun stopGameRunning() {
        gameThread?.join(endGameTimeout)
        ScreenManager.disposeAll()
    }

    private fun startGameRunning() {
        stopGameRunning()
        gameThread = Thread {
            while (isRunning) {
                if (surfaceCreated) {
                    synchronized(surfaceView.holder) {
                        val canvas = surfaceView.holder.lockCanvas()
                        ScreenManager.gameLoop(canvas)
                        surfaceView.holder.unlockCanvasAndPost(canvas)
                    }
                }
            }
        }
        gameThread?.start()
    }

    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {}

    override fun surfaceCreated(holder: SurfaceHolder?) {
        surfaceCreated = true
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        surfaceCreated = false
    }
}