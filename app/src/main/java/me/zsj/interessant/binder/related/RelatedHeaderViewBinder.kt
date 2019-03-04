package me.zsj.interessant.binder.related

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.jakewharton.rxbinding2.view.RxView
import me.drakeet.multitype.ItemViewBinder
import me.zsj.interessant.MainActivity.Companion.CATEGORY_ID
import me.zsj.interessant.R
import me.zsj.interessant.VideoListActivity
import me.zsj.interessant.interesting.InterestingActivity
import me.zsj.interessant.utils.IDUtils
import java.util.concurrent.TimeUnit

class RelatedHeaderViewBinder : ItemViewBinder<RelatedHeader, RelatedHeaderViewBinder.RelatedHeaderHolder>() {
    override fun onCreateViewHolder(
        inflater: LayoutInflater,parent: ViewGroup): RelatedHeaderViewBinder.RelatedHeaderHolder {
        return RelatedHeaderHolder(inflater.inflate(R.layout.item_category_header, parent, false))
    }

    override fun onBindViewHolder(holder: RelatedHeaderViewBinder.RelatedHeaderHolder, relatedHeader: RelatedHeader) {
        val header = relatedHeader.header
        holder.categoryTitle.setText(header.title)
        RxView.clicks(holder.content)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe { aVoid -> toInteresting(holder.content.context, header.id, relatedHeader.related) }
    }

    private fun toInteresting(context: Context, id: Int, related: Boolean) {
        if (IDUtils.isDetermined(id) && !related) {
            val videoIntent = Intent(context, VideoListActivity::class.java)
            videoIntent.putExtra("id", id)
            videoIntent.putExtra("newest", true)
            context.startActivity(videoIntent)
        } else {
            val interestingIntent = Intent(context, InterestingActivity::class.java)
            interestingIntent.putExtra(CATEGORY_ID, id)
            interestingIntent.putExtra(InterestingActivity.RELATED_VIDEO, true)
            context.startActivity(interestingIntent)
        }
    }

    class RelatedHeaderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var content: FrameLayout
        var categoryTitle: TextView

        init {
            content = itemView.findViewById<View>(R.id.category_content) as FrameLayout
            categoryTitle = itemView.findViewById<View>(R.id.category_title) as TextView
        }
    }
}