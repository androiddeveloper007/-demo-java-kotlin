package me.zsj.interessant.binder.related

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jakewharton.rxbinding2.view.RxView
import me.zsj.interessant.IntentManager
import me.zsj.interessant.R
import me.zsj.interessant.model.ItemList
import me.zsj.interessant.widget.RatioImageView
import java.util.concurrent.TimeUnit

class CardAdapter(context: Activity): RecyclerView.Adapter<CardAdapter.CardHolder>() {

    private var items: List<ItemList>? = null
    private val mContext: Activity = context

    internal fun setItems(items: List<ItemList>) {
        this.items = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        return CardHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_related_card, parent,false))
    }

    override fun getItemCount(): Int {
        return items!!.size
    }

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        val item = items!!.get(position)
        holder.imageView.setOriginalSize(5, 3)
        Glide.with(holder.imageView.context)
            .load(item.data.cover.detail)
            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
            .into(holder.imageView)
        holder.desc.setText(item.data.title)
        RxView.clicks(holder.content)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe { aVoid -> fly(holder.imageView, item) }
    }

    private fun fly(view: View, item: ItemList) {
        IntentManager.flyToMovieDetail(mContext, item, view)
    }

    class CardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var content: FrameLayout
        var imageView: RatioImageView
        var desc: TextView
        init {
            content = itemView.findViewById<View>(R.id.content) as FrameLayout
            imageView = itemView.findViewById<View>(R.id.movie_album) as RatioImageView
            desc = itemView.findViewById<View>(R.id.related_title) as TextView
        }
    }
}