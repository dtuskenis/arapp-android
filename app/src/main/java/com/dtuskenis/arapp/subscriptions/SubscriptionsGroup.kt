package com.dtuskenis.arapp.subscriptions

interface SubscriptionsGroup {

    fun <Listener> add(subscribeable: Subscribable<Listener>,
                       listener: Listener): SubscriptionsGroup
}