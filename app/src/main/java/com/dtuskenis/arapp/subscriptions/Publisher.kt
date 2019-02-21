package com.dtuskenis.arapp.subscriptions

class Publisher<Listener>: Subscribable<Listener> {

    private val listeners = mutableListOf<Listener>()

    override fun subscribe(listener: Listener): Unsubscribable {
        listeners.add(listener)

        return object : Unsubscribable {
            override fun unsubscribe() {
                listeners.remove(listener)
            }
        }
    }

    fun publish(action: (Listener) -> Unit) {
        val listenersToNotify = listeners.toList()

        listenersToNotify.forEach { action(it) }
    }
}

fun <Listener: () -> Unit> Publisher<Listener>.publish() = publish { it() }

fun <Data, Listener: (Data) -> Unit> Publisher<Listener>.publish(data: Data) = publish { it(data) }
