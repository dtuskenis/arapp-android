package com.dtuskenis.arapp.views

import com.dtuskenis.arapp.lifecycle.Lifecycle
import com.dtuskenis.arapp.functional.CancellableTracker

class AppPresenter(lifecycle: Lifecycle,
                   view: AppView,
                   model: AppModel) {

    private val currentLoading = CancellableTracker()

    init {
        lifecycle.subscriptions
            .add(view.onRenderableRequested()) {
                lifecycle.launch {
                    model.loadRenderableNamed(it.name).let {
                        view.acceptRequestedRenderable(it)
                    }
                }.also { currentLoading.replace(it) }
            }
            .add(view.onSwitchedToTrackingMode()) {
                currentLoading.clear()
            }

        lifecycle.launch {
            model.loadItems().let { view.setCatalogueItems(it) }
        }
    }
}