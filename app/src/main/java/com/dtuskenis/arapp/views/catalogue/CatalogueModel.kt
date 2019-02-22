package com.dtuskenis.arapp.views.catalogue

import android.content.res.AssetManager
import android.net.Uri
import com.dtuskenis.arapp.functional.Run
import com.dtuskenis.arapp.images.Image
import com.dtuskenis.arapp.views.Coordinator
import com.dtuskenis.arapp.subscriptions.Subscribable

class CatalogueModel(private val assetsManager: AssetManager,
                     private val coordinator: Coordinator) {

    fun onUnlocked(): Subscribable<Run> = coordinator.onCatalogueUnlocked()

    fun getItems(): List<CatalogueItem> {
        val names = (assetsManager.list("models") ?: emptyArray()).toList()
        return (1..5)
                .flatMap { names }
                .map {
                    CatalogueItem(it,
                                  Image(Uri.parse("file:///android_asset/models/$it/preview.png")))
                }
    }

    fun selectItem(item: CatalogueItem) {
        coordinator.loadModelOf(item)
    }
}