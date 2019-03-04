package me.zsj.interessant.common

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import me.zsj.interessant.R
import me.zsj.interessant.widget.RatioImageView

class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var movieAlbum: RatioImageView
    var movieDesc: TextView
    var movieContent: FrameLayout
    var tag: TextView

    init {
        movieContent = itemView.findViewById(R.id.movie_content) as FrameLayout
        movieAlbum = itemView.findViewById(R.id.movie_album) as RatioImageView
        movieDesc = itemView.findViewById(R.id.movie_desc) as TextView
        tag = itemView.findViewById(R.id.author_tag) as TextView
    }
}