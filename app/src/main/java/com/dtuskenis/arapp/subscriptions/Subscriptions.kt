package com.dtuskenis.arapp.subscriptions

import com.dtuskenis.arapp.functional.Run

object Subscriptions {

    fun <Listener> trackListenersOf(subscribable: Subscribable<Listener>,
                                    onNoMoreListeners: Run): Subscribable<Listener> {
        var numberOfListeners = 0

        return object : Subscribable<Listener> {
            override fun subscribe(listener: Listener): Unsubscribable {

                val innerSubscription = subscribable.subscribe(listener)

                numberOfListeners++

                return object : Unsubscribable {
                    override fun unsubscribe() {
                        innerSubscription.unsubscribe()

                        numberOfListeners--

                        if (numberOfListeners == 0) {
                            onNoMoreListeners()
                        }
                    }
                }
            }
        }
    }
}