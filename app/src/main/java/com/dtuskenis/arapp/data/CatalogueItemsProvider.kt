package com.dtuskenis.arapp.data

import android.content.res.AssetManager
import android.net.Uri
import com.dtuskenis.arapp.images.Image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CatalogueItemsProvider(private val assetsManager: AssetManager) {

    suspend fun loadItems(): List<RenderableInfo> {
        val names = withContext(Dispatchers.IO) {
            (assetsManager.list("models") ?: emptyArray()).toList()
        }
        return (1..5)
            .flatMap { names }
            .map {
                RenderableInfo(
                    it,
                    Image(Uri.parse("file:///android_asset/models/$it/preview.png"))
                )
            }
    }
}