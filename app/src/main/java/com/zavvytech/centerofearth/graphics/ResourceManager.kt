package com.zavvytech.centerofearth.graphics

import android.content.res.Resources
import android.support.annotation.DrawableRes

object ResourceManager {

    var resources: Resources? = null
    private val textures: HashMap<Int, CounterFor<Texture>> = HashMap()

    fun getTexture(@DrawableRes resId: Int): Texture {
        if (resources == null) throw NullPointerException("Resources not yet initialised")

        val prevValue = if (textures.containsKey(resId)) {
            textures[resId]
        } else {
            CounterFor(Texture(resId, resources!!), 0)
        }!!
        prevValue.increment()
        textures[resId] = prevValue
        return prevValue.t
    }

    fun notifyDisposed(resId: Int) {
        textures[resId]?.decrement()
        if (!textures[resId]?.isUsed()!!) {
            textures.remove(resId)
        }
    }

    data class CounterFor<T>(val t: T, var count: Int) {
        fun increment() {
            count += 1
        }

        fun decrement() {
            count -= 1
        }

        fun isUsed(): Boolean {
            return count <= 0
        }
    }
}