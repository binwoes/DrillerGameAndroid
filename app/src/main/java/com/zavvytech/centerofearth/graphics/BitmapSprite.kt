package com.zavvytech.centerofearth.graphics

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF

class BitmapSprite(var texture: Texture, var paint: Paint? = null): Sprite {
    private val matrix = Matrix()

    fun draw(canvas: Canvas, dst: RectF, rotationDegrees: Float) {
        matrix.setRectToRect(texture.rect, dst, Matrix.ScaleToFit.FILL)
        matrix.postRotate(rotationDegrees, dst.centerX(), dst.centerY())
        canvas.drawBitmap(texture.bitmap, matrix, paint)
    }
}