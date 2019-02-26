package com.dtuskenis.arapp.images

import android.content.Context
import android.support.v4.widget.CircularProgressDrawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.dtuskenis.arapp.functional.Cancel

class GlideImageLoader(private val context: Context): ImageLoader {

    private val glide = Glide.with(context)

    override fun load(image: Image,
                      target: ImageView): Cancel {
        val viewTarget =
                glide.load(image.uri)
                        .centerCrop()
                        .placeholder(CircularProgressDrawable(context))
                        .into(target)

        return { glide.clear(viewTarget) }
    }
}
