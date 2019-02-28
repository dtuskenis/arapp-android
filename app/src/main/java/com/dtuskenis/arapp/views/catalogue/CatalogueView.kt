package com.dtuskenis.arapp.views.catalogue

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.DecelerateInterpolator
import com.dtuskenis.arapp.R
import com.dtuskenis.arapp.data.RenderableInfo
import com.dtuskenis.arapp.functional.Accept
import com.dtuskenis.arapp.functional.Run
import com.dtuskenis.arapp.subscriptions.Publisher
import com.dtuskenis.arapp.subscriptions.Subscribable
import com.dtuskenis.arapp.subscriptions.publish

class CatalogueView(rootView: View) {

    private val onItemSelected = Publisher<Accept<RenderableInfo>>()
    private val onDismissed = Publisher<Run>()

    private val itemsAdapter: CatalogItemsAdapter

    private val background = rootView.findViewById<View>(R.id.background)
    private val itemsContainer = rootView.findViewById<View>(R.id.items_container)

    init {
        background.setOnClickListener {
            updateState(isOpened = false, animated = true)

            onDismissed.publish()
        }

        itemsAdapter = CatalogItemsAdapter {
            updateState(isOpened = false, animated = true)

            onItemSelected.publish(it)
        }

        rootView.findViewById<RecyclerView>(R.id.recycler_view).run {
            layoutManager = LinearLayoutManager(context,
                                                LinearLayoutManager.HORIZONTAL,
                                                false)

            adapter = itemsAdapter
        }

        updateState(isOpened = false, animated = false)
    }

    fun showUp() {
        updateState(isOpened = true, animated = true)
    }

    fun onItemSelected(): Subscribable<Accept<RenderableInfo>> = onItemSelected

    fun onDismissed(): Subscribable<Run> = onDismissed

    fun setItems(items: List<RenderableInfo>) = itemsAdapter.setItems(items)

    private fun updateState(isOpened: Boolean, animated: Boolean) {
        background.isClickable = isOpened

        val backgroundAlpha = if (isOpened) 1F else 0F
        val updateItemsContainerVisibility = { itemsContainer.setVisibleOrGoneIf(!isOpened) }

        if (animated) {
            background.animateDecelerated { it.alpha(backgroundAlpha) }

            animateItemsContainer(isOpened, updateItemsContainerVisibility)
        } else {
            background.alpha = backgroundAlpha

            updateItemsContainerVisibility()
        }
    }

    private fun animateItemsContainer(shouldBeOpened: Boolean, onEnd: () -> Unit) {
        val itemContainerHeight = itemsContainer.height.toFloat()
        val itemContainerTargetTranslationY = if (shouldBeOpened) 0F else itemContainerHeight

        itemsContainer.translationY = itemContainerHeight - itemContainerTargetTranslationY
        itemsContainer.visibility = View.VISIBLE
        itemsContainer.animateDecelerated {
            it.translationY(itemContainerTargetTranslationY).withEndAction { onEnd() }
        }
    }

    companion object {
        private const val DECELERATE_FACTOR = 2.0f

        private fun View.animateDecelerated(animation: (ViewPropertyAnimator) -> ViewPropertyAnimator) =
                animation(animate())
                        .setInterpolator(DecelerateInterpolator(DECELERATE_FACTOR))
                        .start()

        private fun View.setVisibleOrGoneIf(condition: Boolean) {
            visibility = if (condition) View.GONE else View.VISIBLE
        }
    }
}