package com.dtuskenis.arapp.views

import com.google.ar.sceneform.rendering.Renderable
import com.dtuskenis.arapp.data.CatalogueItemsProvider
import com.dtuskenis.arapp.data.RenderablesProvider
import com.dtuskenis.arapp.data.RenderableInfo

class AppModel(private val catalogueItemsProvider: CatalogueItemsProvider,
               private val renderablesProvider: RenderablesProvider) {

    suspend fun loadItems(): List<RenderableInfo> = catalogueItemsProvider.loadItems()

    suspend fun loadRenderableNamed(name:String): Renderable = renderablesProvider.loadRenderableNamed(name)
}