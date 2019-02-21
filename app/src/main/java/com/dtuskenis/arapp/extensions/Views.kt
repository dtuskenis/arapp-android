package com.dtuskenis.arapp.extensions

import android.view.View

fun View.onLaidOut(onLaidOutAction: () -> Unit) {
    if (isLaidOut) {
        onLaidOutAction()
    } else {
        val onLayoutChangeListener = object : View.OnLayoutChangeListener {
            override fun onLayoutChange(view: View,
                                        left: Int,
                                        top: Int,
                                        right: Int,
                                        bottom: Int,
                                        oldLeft: Int,
                                        oldTop: Int,
                                        oldRight: Int,
                                        oldBottom: Int) {
                view.removeOnLayoutChangeListener(this)
                onLaidOutAction()
            }
        }
        addOnLayoutChangeListener(onLayoutChangeListener)
    }
}