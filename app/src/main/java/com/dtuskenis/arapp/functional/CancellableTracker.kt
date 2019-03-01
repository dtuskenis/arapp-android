package com.dtuskenis.arapp.functional

class CancellableTracker {

    private var currentCancellation: Cancel? = null

    fun replace(newCancellation: Cancel) {
        clear()
        currentCancellation = newCancellation
    }

    fun clear() {
        currentCancellation?.invoke()
        currentCancellation = null
    }
}