package com.dtuskenis.arapp.views.ar

import com.dtuskenis.arapp.views.OnNewRenderableListener
import com.dtuskenis.arapp.views.Renderable
import com.dtuskenis.arapp.subscriptions.SubscriptionsGroup

class ArPresenter(subscriptions: SubscriptionsGroup,
                  view: ArView,
                  model: ArModel) {

    init {
        subscriptions
                .add(model.onNewRenderable(),
                     object : OnNewRenderableListener {
                         override fun onLoadingNewRenderable() =
                                 view.showLoading()

                         override fun onRenderableLoaded(renderable: Renderable) =
                                 view.showNewRenderable(renderable)
                     }
                    )
                .add(view.onCommittedOrCancelled()) {
                    model.unlockCatalogue()
                }
    }
}