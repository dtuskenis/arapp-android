package com.dtuskenis.arapp.catalogue

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dtuskenis.arapp.R
import com.dtuskenis.arapp.functional.Accept
import com.dtuskenis.arapp.images.LazyLoadImageView

class CatalogItemsAdapter(private val onItemSelected: Accept<CatalogueItem>): RecyclerView.Adapter<CatalogItemsAdapter.ViewHolder>() {

    class ViewHolder internal constructor(rootView: View,
                                          private val onItemSelected: Accept<CatalogueItem>
    ): RecyclerView.ViewHolder(rootView) {

        private val imageView = rootView.findViewById<LazyLoadImageView>(R.id.image_view)

        fun update(item: CatalogueItem) {
            imageView.setImage(item.image)

            itemView.setOnClickListener { onItemSelected(item) }
        }
    }

    private val items = mutableListOf<CatalogueItem>()

    fun setItems(newItems: List<CatalogueItem>) {
        items.clear()
        items.addAll(newItems)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.view_catalog_item,
                parent,
                false
            ),
            onItemSelected
        )

    override fun getItemCount(): Int =
            items.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
            viewHolder.update(items[position])
}