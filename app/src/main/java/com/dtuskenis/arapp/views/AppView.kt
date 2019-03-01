package com.dtuskenis.arapp.views

import com.google.ar.sceneform.rendering.Renderable
import com.dtuskenis.arapp.lifecycle.Lifecycle
import com.dtuskenis.arapp.functional.Accept
import com.dtuskenis.arapp.subscriptions.Publisher
import com.dtuskenis.arapp.subscriptions.Subscribable
import com.dtuskenis.arapp.subscriptions.publish
import com.dtuskenis.arapp.views.ar.ArView
import com.dtuskenis.arapp.data.RenderableInfo
import com.dtuskenis.arapp.functional.Run
import com.dtuskenis.arapp.views.catalogue.CatalogueView

class AppView(lifecycle: Lifecycle,
              private val arView: ArView,
              private val catalogueView: CatalogueView) {

    private val onRenderableRequested = Publisher<Accept<RenderableInfo>>()

    init {
        lifecycle.subscriptions
                .add(catalogueView.onItemSelected()) {
                    arView.switchToFittingModeWithLoadingIndicator()

                    onRenderableRequested.publish(it)
                }
                .add(arView.onSwitchedToTrackingMode()) {
                    catalogueView.allowSelecting()
                }
    }

    fun setCatalogueItems(items: List<RenderableInfo>) {
        catalogueView.setItems(items)
    }

    fun onRenderableRequested(): Subscribable<Accept<RenderableInfo>> = onRenderableRequested

    fun onSwitchedToTrackingMode(): Subscribable<Run> = arView.onSwitchedToTrackingMode()

    fun acceptRequestedRenderable(renderable: Renderable) {
        arView.switchToFittingModeWithRenderable(renderable)
    }
}