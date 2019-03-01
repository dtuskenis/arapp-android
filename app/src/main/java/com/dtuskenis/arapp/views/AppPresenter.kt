package com.dtuskenis.arapp.views

import com.dtuskenis.arapp.lifecycle.Lifecycle
import com.dtuskenis.arapp.functional.Cancel

class AppPresenter(lifecycle: Lifecycle,
                   view: AppView,
                   model: AppModel) {

    // TODO: need a slot or something
    private var cancelCurrentLoading: Cancel? = null

    init {
        lifecycle.subscriptions
            .add(view.onRenderableRequested()) {
                cancelCurrentLoading?.invoke()
                cancelCurrentLoading = lifecycle.launch {
                    model.loadRenderableNamed(it.name).let {
                        view.acceptRequestedRenderable(it)
                    }
                }
            }
            .add(view.onSwitchedToTrackingMode()) {
                cancelCurrentLoading?.invoke()
                cancelCurrentLoading = null
            }

        lifecycle.launch {
            model.loadItems().let { view.setCatalogueItems(it) }
        }
    }
}