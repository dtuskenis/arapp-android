package com.dtuskenis.arapp.subscriptions

class SubscriptionsGroupControl: SubscriptionsGroup {

    private val unsubscribables = mutableListOf<Unsubscribable>()

    override fun <Listener> add(subscribeable: Subscribable<Listener>,
                                listener: Listener): SubscriptionsGroup {
        unsubscribables.add(subscribeable.subscribe(listener))

        return this
    }

    fun unsubscribeAll() {
        unsubscribables.forEach { it.unsubscribe() }
    }
}