package com.zavvytech.centerofearth.graphics

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

class BitmapSprite(var texture: Texture, var paint: Paint? = null): Sprite {

    fun draw(canvas: Canvas, dst: RectF) {
        canvas.drawBitmap(texture.bitmap, texture.rect, dst, paint)
    }
}