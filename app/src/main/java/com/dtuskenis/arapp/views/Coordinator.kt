package com.dtuskenis.arapp.views

import android.content.Context
import android.content.res.AssetManager
import com.google.ar.sceneform.rendering.ModelRenderable
import com.dtuskenis.arapp.views.catalogue.CatalogueItem
import com.dtuskenis.arapp.functional.Run
import com.dtuskenis.arapp.subscriptions.Publisher
import com.dtuskenis.arapp.subscriptions.Subscribable
import com.dtuskenis.arapp.subscriptions.Subscriptions
import com.dtuskenis.arapp.subscriptions.publish
import java.util.concurrent.CompletableFuture

class Coordinator(private val context: Context,
                  private val assetsManager: AssetManager) {

    private val onNewRenderable = Publisher<OnNewRenderableListener>()

    private val onCatalogueUnlocked = Publisher<Run>()

    private var currentRenderableLoadingTask: CompletableFuture<Void>? = null

    fun loadModelOf(item: CatalogueItem) {
        onNewRenderable.publish { it.onLoadingNewRenderable() }

        cancelCurrentRenderableLoading()
        currentRenderableLoadingTask =
                loadRenderable(item.name).thenAccept { arCoreRenderable ->
                    onNewRenderable.publish { it.onRenderableLoaded(Renderable(arCoreRenderable)) }
                    currentRenderableLoadingTask = null
                }
    }

    fun unlockCatalogue() = onCatalogueUnlocked.publish()

    fun onCatalogueUnlocked(): Subscribable<Run> = onCatalogueUnlocked

    fun onNewRenderable(): Subscribable<OnNewRenderableListener> =
            Subscriptions.trackListenersOf(onNewRenderable,
                                           onNoMoreListeners = { cancelCurrentRenderableLoading() })

    private fun cancelCurrentRenderableLoading() {
        currentRenderableLoadingTask?.cancel(true)
        currentRenderableLoadingTask = null
    }

    private fun loadRenderable(renderableName: String): CompletableFuture<ModelRenderable> =
            ModelRenderable
                    .builder()
                    .setSource(context) { assetsManager.open("models/${renderableName}/renderable.sfb") }
                    .build()
}