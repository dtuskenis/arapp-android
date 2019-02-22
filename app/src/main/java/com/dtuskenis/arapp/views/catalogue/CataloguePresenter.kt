package com.dtuskenis.arapp.views.catalogue

import com.dtuskenis.arapp.subscriptions.SubscriptionsGroup

class CataloguePresenter(subscriptions: SubscriptionsGroup,
                         view: CatalogueView,
                         model: CatalogueModel) {

    init {
        subscriptions
                .add(view.onCatalogueOpened()) {
                    model.getItems().let { view.setItems(it) }
                }
                .add(view.onItemSelected()) {
                    model.selectItem(it)
                }
                .add(model.onUnlocked()) {
                    view.unlock()
                }
    }
}