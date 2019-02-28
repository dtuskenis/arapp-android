package com.dtuskenis.arapp.views

import com.google.ar.sceneform.rendering.Renderable
import com.dtuskenis.arapp.data.CatalogueItemsProvider
import com.dtuskenis.arapp.data.RenderablesProvider
import com.dtuskenis.arapp.functional.Accept
import com.dtuskenis.arapp.functional.Cancel
import com.dtuskenis.arapp.data.RenderableInfo

class AppModel(private val catalogueItemsProvider: CatalogueItemsProvider,
               private val renderablesProvider: RenderablesProvider) {

    fun loadItems(onCompleted: Accept<List<RenderableInfo>>) {
        onCompleted(catalogueItemsProvider.loadItems())
    }

    fun loadRenderableNamed(name:String,
                            onCompleted: Accept<Renderable>): Cancel = renderablesProvider.loadRenderableNamed(name,
                                                                                                               onCompleted)
}