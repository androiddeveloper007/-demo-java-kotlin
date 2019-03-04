package me.zsj.interessant.binder.daily

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import me.drakeet.multitype.ItemViewBinder
import me.zsj.interessant.R
import me.zsj.interessant.model.Category

class CategoryViewBinder : ItemViewBinder<Category, CategoryViewBinder.DateHolder>() {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): DateHolder {
        val view = inflater.inflate(R.layout.item_category_title, parent, false)
        return DateHolder(view)
    }

    override fun onBindViewHolder(holder: DateHolder, item: Category) {
        holder.category.text = item.categoryTitle
    }

    class DateHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val category: TextView = itemView.findViewById(R.id.date) as TextView
    }
}