package com.example.project1.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.project1.R
import com.example.project1.classes.Category

class CategoryAdapterS(
    private val context: Context,
    private val items: List<Category>
) : BaseAdapter() {

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.category_details, parent, false)

        val currentItem = items[position]

        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        val itemCountTextView: TextView = view.findViewById(R.id.itemCountTextView)
        val imageView: ImageView = view.findViewById(R.id.itemImageView)
        val iconView: ImageView = view.findViewById(R.id.iconImageView)

        nameTextView.text = currentItem.title
        itemCountTextView.text = currentItem.taskNumber
        imageView.setImageResource(currentItem.image)
        iconView.setImageResource(R.drawable.right) // Utilisez l'image appropriée

        return view
    }
}
