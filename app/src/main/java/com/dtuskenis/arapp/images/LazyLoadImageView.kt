package com.dtuskenis.arapp.images

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.dtuskenis.arapp.App
import com.dtuskenis.arapp.functional.CancellableTracker

class LazyLoadImageView: ImageView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var currentImage: Image? = null
    private val currentLoading = CancellableTracker()

    fun setImage(image: Image) {
        clear()

        currentImage = image

        if (isAttachedToWindow) {
            loadCurrentImageIfAny()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        loadCurrentImageIfAny()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        clear()
    }

    private fun loadCurrentImageIfAny() {
        currentImage?.let { load(it) }
    }

    private fun load(image: Image) {
        imageLoader.load(image, this)
                   .also { currentLoading.replace(it) }
    }

    private fun clear() {
        currentLoading.clear()

        setImageBitmap(null)
    }

    companion object {
        private val imageLoader = App.getServices().imageLoader
    }
}