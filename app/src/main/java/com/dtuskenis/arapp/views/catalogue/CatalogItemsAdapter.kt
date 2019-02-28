package com.dtuskenis.arapp.views.catalogue

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dtuskenis.arapp.R
import com.dtuskenis.arapp.data.RenderableInfo
import com.dtuskenis.arapp.functional.Accept
import com.dtuskenis.arapp.images.LazyLoadImageView

internal class CatalogItemsAdapter(private val onItemSelected: Accept<RenderableInfo>): RecyclerView.Adapter<CatalogItemsAdapter.ViewHolder>() {

    class ViewHolder internal constructor(rootView: View,
                                          private val onItemSelected: Accept<RenderableInfo>): RecyclerView.ViewHolder(rootView) {

        private val imageView = rootView.findViewById<LazyLoadImageView>(R.id.image_view)

        fun update(item: RenderableInfo) {
            imageView.setImage(item.previewImage)

            itemView.setOnClickListener { onItemSelected(item) }
        }
    }

    private val items = mutableListOf<RenderableInfo>()

    fun setItems(newItems: List<RenderableInfo>) {
        items.clear()
        items.addAll(newItems)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_catalog_item,
                                                                   parent,
                                                                   false),
                       onItemSelected)

    override fun getItemCount(): Int =
            items.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
            viewHolder.update(items[position])
}