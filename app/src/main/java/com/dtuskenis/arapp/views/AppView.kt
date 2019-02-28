package com.dtuskenis.arapp.views

import com.google.ar.sceneform.rendering.Renderable
import com.dtuskenis.arapp.functional.Accept
import com.dtuskenis.arapp.subscriptions.Publisher
import com.dtuskenis.arapp.subscriptions.Subscribable
import com.dtuskenis.arapp.subscriptions.SubscriptionsGroup
import com.dtuskenis.arapp.subscriptions.publish
import com.dtuskenis.arapp.views.ar.ArView
import com.dtuskenis.arapp.data.RenderableInfo
import com.dtuskenis.arapp.functional.Run
import com.dtuskenis.arapp.views.catalogue.CatalogueView

class AppView(subscriptions: SubscriptionsGroup,
              private val arView: ArView,
              private val catalogueView: CatalogueView) {

    private val onRenderableRequested = Publisher<Accept<RenderableInfo>>()

    init {
        subscriptions
                .add(catalogueView.onItemSelected()) {
                    arView.switchToFittingModeWithLoadingIndicator()

                    onRenderableRequested.publish(it)
                }
                .add(catalogueView.onDismissed()) {
                    arView.makeAddButtonVisible()
                }
                .add(arView.onAddButtonClickedAndHidden()) {
                    catalogueView.showUp()
                }
    }

    fun setCatalogueItems(items: List<RenderableInfo>) {
        catalogueView.setItems(items)
    }

    fun onRenderableRequested(): Subscribable<Accept<RenderableInfo>> = onRenderableRequested

    fun onRenderableRequestCancelled(): Subscribable<Run> = arView.onFittingModeCancelled()

    fun acceptRequestedRenderable(renderable: Renderable) {
        arView.switchToFittingModeWithRenderable(renderable)
    }
}