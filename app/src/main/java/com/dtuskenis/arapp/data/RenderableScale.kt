package com.dtuskenis.arapp.data

data class RenderableScale(val floatValue: Float) {

    init {
        if (floatValue !in ALLOWED_FLOAT_VALUES) {
            throw RuntimeException("$floatValue !in $ALLOWED_FLOAT_VALUES")
        }
    }

    companion object {
        val ALLOWED_FLOAT_VALUES = 0.5f..4.0f
    }
}