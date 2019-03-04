package me.zsj.interessant.binder.video

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jakewharton.rxbinding2.view.RxView
import me.drakeet.multitype.ItemViewBinder
import me.zsj.interessant.R
import me.zsj.interessant.VideoListActivity
import java.util.concurrent.TimeUnit

class FooterForwardViewBinder: ItemViewBinder<FooterForward, FooterForwardViewBinder.Holder>() {
    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): FooterForwardViewBinder.Holder {
        val view = inflater.inflate(R.layout.item_forward_footer, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: FooterForwardViewBinder.Holder, item: FooterForward) {
        holder.footerText.text=item.text
        RxView.clicks(holder.footerText)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe{aVoid -> toVideoList(holder.footerText.context,item.id)}
    }

    private fun toVideoList(context: Context, id:Int){
        val intent = Intent(context, VideoListActivity::class.java)
        intent.putExtra("id",id)
        intent.putExtra("trending", true)
        context.startActivity(intent)

    }
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val footerText: TextView = itemView.findViewById(R.id.footer_text)
    }
}