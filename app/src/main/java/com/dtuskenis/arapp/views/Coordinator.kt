package com.dtuskenis.arapp.views

import com.dtuskenis.arapp.functional.Cancel
import com.dtuskenis.arapp.views.catalogue.CatalogueItem
import com.dtuskenis.arapp.functional.Run
import com.dtuskenis.arapp.subscriptions.Publisher
import com.dtuskenis.arapp.subscriptions.Subscribable
import com.dtuskenis.arapp.subscriptions.Subscriptions
import com.dtuskenis.arapp.subscriptions.publish

class Coordinator(private val renderablesProvider: RenderablesProvider) {

    private val onNewRenderable = Publisher<OnNewRenderableListener>()

    private val onCatalogueUnlocked = Publisher<Run>()

    private var cancelCurrentLoading: Cancel? = null

    fun loadModelOf(item: CatalogueItem) {
        onNewRenderable.publish { it.onLoadingNewRenderable() }

        cancelCurrentRenderableLoading()
        cancelCurrentLoading =
                renderablesProvider.getRenderableNamed(item.name) { renderable ->
                    onNewRenderable.publish { it.onRenderableLoaded(renderable) }
                    cancelCurrentLoading = null
                }
    }

    fun unlockCatalogue() = onCatalogueUnlocked.publish()

    fun onCatalogueUnlocked(): Subscribable<Run> = onCatalogueUnlocked

    fun onNewRenderable(): Subscribable<OnNewRenderableListener> =
            Subscriptions.trackListenersOf(onNewRenderable,
                                           onNoMoreListeners = { cancelCurrentRenderableLoading() })

    private fun cancelCurrentRenderableLoading() {
        cancelCurrentLoading?.invoke()
        cancelCurrentLoading = null
    }
}