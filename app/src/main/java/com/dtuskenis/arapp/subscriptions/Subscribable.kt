package com.dtuskenis.arapp.subscriptions

interface Subscribable<Listener> {

    fun subscribe(listener: Listener): Unsubscribable
}