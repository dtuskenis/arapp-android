package com.dtuskenis.arapp.images

import android.widget.ImageView
import com.dtuskenis.arapp.functional.Cancel

interface ImageLoader {

    fun load(image: Image,
             target: ImageView): Cancel
}