package com.dtuskenis.arapp.views

import com.google.ar.sceneform.rendering.Renderable

class Renderable(val arCoreRenderable: Renderable) {

    data class ScaleMultiplier(val floatValue: Float) {

        init {
            if (floatValue !in ALLOWED_FLOAT_VALUES) {
                throw RuntimeException("$floatValue !in $ALLOWED_FLOAT_VALUES")
            }
        }

        companion object {
            val ALLOWED_FLOAT_VALUES = 0.5f..4.0f
        }
    }
}