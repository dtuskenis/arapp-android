package com.dtuskenis.arapp

import android.app.Application
import com.dtuskenis.arapp.images.GlideImageLoader
import com.dtuskenis.arapp.images.ImageLoader

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        services = Services(imageLoader = GlideImageLoader(this))
    }

    class Services(val imageLoader: ImageLoader)

    companion object {

        private var services: Services? = null

        fun getServices(): Services = services!!
    }
}