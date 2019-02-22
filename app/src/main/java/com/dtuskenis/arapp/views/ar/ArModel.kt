package com.dtuskenis.arapp.views.ar

import com.dtuskenis.arapp.views.OnNewRenderableListener
import com.dtuskenis.arapp.views.Coordinator
import com.dtuskenis.arapp.subscriptions.Subscribable

class ArModel(private val coordinator: Coordinator) {

    fun unlockCatalogue() {
        coordinator.unlockCatalogue()
    }

    fun onNewRenderable(): Subscribable<OnNewRenderableListener> = coordinator.onNewRenderable()

}