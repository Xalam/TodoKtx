package com.example.todo.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.data.Category
import com.example.todo.databinding.ItemCategoryBinding
import com.example.todo.utils.ItemClick

class CategoryAdapter: RecyclerView.Adapter<CategoryAdapter.ListViewHolder>() {

    private var categoryList = emptyList<Category>()
    private lateinit var itemClick: ItemClick

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentItem = categoryList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    inner class ListViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(category: Category) {
                with(binding) {
                    if (category.id == 0) {
                        circleCat.visibility = View.GONE
                        textCat.visibility = View.GONE
                        buttonDeleteCat.visibility = View.GONE
                    }

                    textCat.text = category.cat_title

                    buttonDeleteCat.setOnClickListener {
                        itemClick.onItemClickStatus(category.id!!, 1)
                    }
                }
        }
    }

    fun setData(category: List<Category>) {
        this.categoryList = category
        notifyDataSetChanged()
    }

    fun setItemClick(item: ItemClick) {
        this.itemClick = item
    }
}