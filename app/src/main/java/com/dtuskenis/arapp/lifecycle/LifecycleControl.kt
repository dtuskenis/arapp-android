package com.dtuskenis.arapp.lifecycle

import com.dtuskenis.arapp.functional.Cancel
import com.dtuskenis.arapp.subscriptions.SubscriptionsGroup
import com.dtuskenis.arapp.subscriptions.SubscriptionsGroupControl
import kotlinx.coroutines.*

class LifecycleControl(dispatcher: CoroutineDispatcher): Lifecycle {

    private val subscriptionsControl = SubscriptionsGroupControl()
    private val coroutineScope = CoroutineScope(SupervisorJob() + dispatcher)

    override val subscriptions: SubscriptionsGroup = subscriptionsControl

    override fun launch(block: suspend () -> Unit): Cancel =
        coroutineScope.launch { block() }.let { job -> { job.cancel() } }

    fun terminate() {
        subscriptionsControl.unsubscribeAll()
        coroutineScope.coroutineContext[Job]?.cancel()
    }
}