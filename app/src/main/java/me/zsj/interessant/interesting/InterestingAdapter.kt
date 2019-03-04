package me.zsj.interessant.interesting

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jakewharton.rxbinding2.view.RxView
import me.zsj.interessant.IntentManager
import me.zsj.interessant.R
import me.zsj.interessant.common.Holder
import me.zsj.interessant.model.ItemList
import java.util.concurrent.TimeUnit

class InterestingAdapter(private val context: Activity, private val itemList: List<ItemList>) :
    RecyclerView.Adapter<Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): Holder {
        val view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_movie, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = itemList[position]

        if (item.type != VIDEO_TYPE) run { holder.movieContent.setVisibility(View.GONE) } else {

            holder.movieAlbum.setOriginalSize(16, 9)
            Glide.with(holder.movieAlbum.getContext())
                .load(item.data.cover.detail)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.movieAlbum)
            holder.movieDesc.setText(item.data.title)

            if (item.data.author != null) {
                holder.tag.setVisibility(View.VISIBLE)
                holder.tag.setText(item.data.author.name)
            } else {
                holder.tag.setVisibility(View.GONE)
            }
            RxView.clicks(holder.movieContent).throttleFirst(TOUCH_TIME.toLong(), TimeUnit.MILLISECONDS)
                .subscribe { aVoid -> fly(holder.movieAlbum, item) }
        }
    }

    private fun fly(view: View, item: ItemList) {
        IntentManager.flyToMovieDetail(context, item, view)
    }

    private val TOUCH_TIME = 1000

    companion object {
        const val VIDEO_TYPE = "video"
    }
}