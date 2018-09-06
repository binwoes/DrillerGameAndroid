package com.zavvytech.centerofearth.graphics

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.support.annotation.DrawableRes

class Texture(@DrawableRes resId: Int, res: Resources){
    val bitmap = BitmapFactory.decodeResource(res, resId)
    val width = bitmap.width
    val height = bitmap.height
}
