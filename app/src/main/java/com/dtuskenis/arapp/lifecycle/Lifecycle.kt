package com.dtuskenis.arapp.lifecycle

import com.dtuskenis.arapp.functional.Cancel
import com.dtuskenis.arapp.subscriptions.SubscriptionsGroup

interface Lifecycle {

    val subscriptions: SubscriptionsGroup

    fun launch(block: suspend () -> Unit): Cancel
}