package com.dtuskenis.arapp.catalogue

import android.content.Context
import android.content.res.AssetManager
import android.net.Uri
import android.widget.Toast
import com.dtuskenis.arapp.images.Image

class CatalogueModel(private val context: Context,
                     private val assetsManager: AssetManager) {

    fun getItems(): List<CatalogueItem> {
        val names = assetsManager.list("models") ?: emptyArray()
        return (1..5)
                .flatMap { names.toList() }
                .map { "file:///android_asset/models/$it/preview.png" }
                .map { Uri.parse(it) }
                .map { CatalogueItem(Image(it)) }
    }

    fun logItemSelected(item: CatalogueItem) {
        Toast.makeText(context,
                       "$item",
                       Toast.LENGTH_SHORT)
                .show()
    }
}