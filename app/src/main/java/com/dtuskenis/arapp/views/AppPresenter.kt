package com.dtuskenis.arapp.views

import com.dtuskenis.arapp.functional.Cancel
import com.dtuskenis.arapp.subscriptions.SubscriptionsGroup

class AppPresenter(subscriptions: SubscriptionsGroup,
                   view: AppView,
                   model: AppModel) {

    // TODO: need a slot or something
    private var cancelCurrentLoading: Cancel? = null

    init {
        subscriptions
            .add(view.onRenderableRequested()) {
                cancelCurrentLoading?.invoke()
                // TODO: tie to lifecycle
                cancelCurrentLoading = model.loadRenderableNamed(it.name) {
                    cancelCurrentLoading = null

                    view.acceptRequestedRenderable(it)
                }
            }
            .add(view.onRenderableRequestCancelled()) {
                cancelCurrentLoading?.invoke()
                cancelCurrentLoading = null
            }

        // TODO: tie to lifecycle
        model.loadItems { view.setCatalogueItems(it) }
    }
}