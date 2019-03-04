package me.zsj.interessant.binder.video

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jakewharton.rxbinding2.view.RxView
import me.drakeet.multitype.ItemViewBinder
import me.zsj.interessant.IntentManager
import me.zsj.interessant.R
import me.zsj.interessant.common.Holder
import me.zsj.interessant.model.ItemList
import java.util.concurrent.TimeUnit

class VideoViewBinder(context: Activity): ItemViewBinder<ItemList, Holder>() {

    var mContext: Activity = context

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): Holder {
        val view = inflater.inflate(R.layout.item_movie, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, item: ItemList) {
        holder.movieAlbum.setOriginalSize(16,9)
        Glide.with(holder.movieAlbum.context)
            .load(item.data.cover.detail)
            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
            .into(holder.movieAlbum)
        holder.movieDesc.text=item.data.text
        if (item.data.author != null) {
            holder.tag.visibility = View.VISIBLE
            holder.tag.text = item.data.author.name
        } else {
            holder.tag.visibility = View.GONE
        }
        RxView.clicks(holder.movieContent)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe{aVoid -> fly(holder.movieAlbum, item)}
    }

    private fun fly(view: View, item: ItemList) {
        IntentManager.flyToMovieDetail(mContext, item, view)
    }
}