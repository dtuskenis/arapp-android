package com.dtuskenis.arapp.views.catalogue

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
    private val onItemSelected = Publisher<Accept<CatalogueItem>>()

    private val itemsAdapter: CatalogItemsAdapter

    private val background = rootView.findViewById<View>(R.id.background)
    private val itemsContainer = rootView.findViewById<View>(R.id.items_container)
    private val addButton = rootView.findViewById<FloatingActionButton>(R.id.add_button)

    init {
        rootView.onLaidOut { changeStateTo(CatalogueViewState.CLOSED, animated = false) }

        background.setOnClickListener { changeStateTo(CatalogueViewState.CLOSED, animated = true) }

        addButton.setOnClickListener {
            changeStateTo(CatalogueViewState.OPENED, animated = true)

            onCatalogueOpened.publish()
        }

        itemsAdapter = CatalogItemsAdapter {
            changeStateTo(CatalogueViewState.LOCKED, animated = true)

            onItemSelected.publish(it)
        }

        rootView.findViewById<RecyclerView>(R.id.recycler_view).run {
            layoutManager = LinearLayoutManager(context,
                                                LinearLayoutManager.HORIZONTAL,
                                                false)

            adapter = itemsAdapter
        }
    }

    fun unlock() {
        changeStateTo(CatalogueViewState.CLOSED, animated = true)
    }

    fun onCatalogueOpened(): Subscribable<Run> = onCatalogueOpened

    fun onItemSelected(): Subscribable<Accept<CatalogueItem>> = onItemSelected

    fun setItems(items: List<CatalogueItem>) = itemsAdapter.setItems(items)

    private fun changeStateTo(newState: CatalogueViewState, animated: Boolean) {
        val catalogueShouldBeVisible = newState == CatalogueViewState.OPENED
        val addButtonShouldBeVisible = newState == CatalogueViewState.CLOSED

        background.isClickable = catalogueShouldBeVisible

        val backgroundAlpha = if (catalogueShouldBeVisible) 1F else 0F
        val itemContainerTranslationY = if (catalogueShouldBeVisible) 0F else itemsContainer.height.toFloat()

        if (animated) {
            addButton.run { if (addButtonShouldBeVisible) show() else hide() }

            background.animateDecelerated { it.alpha(backgroundAlpha) }
            itemsContainer.animateDecelerated { it.translationY(itemContainerTranslationY) }
        } else {
            addButton.visibility = if (addButtonShouldBeVisible) View.VISIBLE else View.GONE

            background.alpha = backgroundAlpha
            itemsContainer.translationY = itemContainerTranslationY
        }
    }

    companion object {
        private const val DECELERATE_FACTOR = 2.0f

        private fun View.animateDecelerated(animation: (ViewPropertyAnimator) -> ViewPropertyAnimator) =
                animation(animate())
                        .setInterpolator(DecelerateInterpolator(DECELERATE_FACTOR))
                        .start()
    }

    private enum class CatalogueViewState {
        LOCKED,
        OPENED,
        CLOSED,
    }
}