package com.atlantis.bankan

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView

class ImageAdapter(
    private val context: Context,
    private val images: Array<Int>,
    private val onImageClick: (Int) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = images.size

    override fun getItem(position: Int): Any = images[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val imageView: ImageView
        if (convertView == null) {
            imageView = LayoutInflater.from(context).inflate(R.layout.grid_item_image, parent, false) as ImageView
            imageView.layoutParams = ViewGroup.LayoutParams(200, 200) // Boyutları ihtiyacınıza göre ayarlayın
        } else {
            imageView = convertView as ImageView
        }

        imageView.setImageResource(images[position])

        imageView.setOnClickListener {
            onImageClick(images[position])
        }

        return imageView
    }
}
