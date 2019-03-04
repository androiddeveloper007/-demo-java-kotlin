package me.zsj.interessant.binder.related

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import me.drakeet.multitype.ItemViewBinder
import me.zsj.interessant.R

class HeaderViewBinder : ItemViewBinder<HeaderItem, HeaderViewBinder.HeaderHolder>() {
    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): HeaderViewBinder.HeaderHolder {
        return HeaderHolder(inflater.inflate(R.layout.item_related_header, parent, false))
    }

    override fun onBindViewHolder(holder: HeaderViewBinder.HeaderHolder, item: HeaderItem) {
        var icon: String
        var title: String
        var description: String
        var id: Int
        if (item.header != null){

        }else{

        }
    }

    class HeaderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var content: RelativeLayout
        var avatar: ImageView
        var relatedTitle: TextView
        var relatedDesc: TextView
        var arrowRight: ImageView

        init {
            content = itemView.findViewById(R.id.header_content) as RelativeLayout
            avatar = itemView.findViewById(R.id.movie_avatar) as ImageView
            relatedTitle = itemView.findViewById(R.id.related_title) as TextView
            relatedDesc = itemView.findViewById(R.id.related_desc) as TextView
            arrowRight = itemView.findViewById(R.id.arrow_right) as ImageView
        }
    }
}