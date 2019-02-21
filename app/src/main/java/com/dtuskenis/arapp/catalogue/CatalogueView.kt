package com.dtuskenis.arapp.catalogue

import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.DecelerateInterpolator
import com.dtuskenis.arapp.R
import com.dtuskenis.arapp.extensions.onLaidOut
import com.dtuskenis.arapp.functional.Accept
import com.dtuskenis.arapp.functional.Run
import com.dtuskenis.arapp.subscriptions.Publisher
import com.dtuskenis.arapp.subscriptions.Subscribable
import com.dtuskenis.arapp.subscriptions.publish

class CatalogueView(rootView: View) {

    private val onCatalogueOpened = Publisher<Run>()
    private val onItemSelected =
        Publisher<Accept<CatalogueItem>>()

    private val itemsAdapter = CatalogItemsAdapter { onItemSelected.publish(it) }

    private val background = rootView.findViewById<View>(R.id.background)
    private val itemsContainer = rootView.findViewById<View>(R.id.items_container)
    private val addButton = rootView.findViewById<FloatingActionButton>(R.id.add_button)

    init {
        rootView.onLaidOut { changeStateUsing(StateChange.CLOSE_NOW) }

        background.setOnClickListener { changeStateUsing(StateChange.CLOSE_ANIMATED) }

        addButton.setOnClickListener {
            changeStateUsing(StateChange.OPEN_ANIMATED)

            onCatalogueOpened.publish()
        }

        rootView.findViewById<RecyclerView>(R.id.recycler_view).run {
            layoutManager = LinearLayoutManager(context,
                                                LinearLayoutManager.HORIZONTAL,
                                                false)

            adapter = itemsAdapter
        }
    }

    fun onCatalogueOpened(): Subscribable<Run> = onCatalogueOpened

    fun onItemSelected(): Subscribable<Accept<CatalogueItem>> = onItemSelected

    fun setItems(items: List<CatalogueItem>) = itemsAdapter.setItems(items)

    private enum class StateChange(val shouldBeOpened: Boolean,
                                   val animated: Boolean) {
        OPEN_ANIMATED   (shouldBeOpened = true,     animated = true),
        CLOSE_ANIMATED  (shouldBeOpened = false,    animated = true),
        OPEN_NOW        (shouldBeOpened = true,     animated = false),
        CLOSE_NOW       (shouldBeOpened = false,    animated = false),
    }

    private fun changeStateUsing(stateChange: StateChange) {
        val shouldBeOpened = stateChange.shouldBeOpened

        background.isClickable = shouldBeOpened

        val backgroundAlpha = if (shouldBeOpened) 1F else 0F
        val itemContainerTranslationY = if (shouldBeOpened) 0F else itemsContainer.height.toFloat()

        if (stateChange.animated) {
            background.animateDecelerated { it.alpha(backgroundAlpha) }
            itemsContainer.animateDecelerated { it.translationY(itemContainerTranslationY) }
        } else {
            background.alpha = backgroundAlpha
            itemsContainer.translationY = itemContainerTranslationY
        }

        addButton.run {
            when (stateChange) {
                StateChange.OPEN_ANIMATED -> hide()
                StateChange.CLOSE_ANIMATED -> show()
                StateChange.OPEN_NOW -> visibility = View.GONE
                StateChange.CLOSE_NOW -> visibility = View.VISIBLE
            }
        }
    }

    companion object {
        private const val DECELERATE_FACTOR = 2.0f

        private fun View.animateDecelerated(animation: (ViewPropertyAnimator) -> ViewPropertyAnimator) =
                animation(animate())
                        .setInterpolator(DecelerateInterpolator(DECELERATE_FACTOR))
                        .start()
    }
}