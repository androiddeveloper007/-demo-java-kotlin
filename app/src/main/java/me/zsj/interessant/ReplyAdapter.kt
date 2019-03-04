package me.zsj.interessant

import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import me.zsj.interessant.model.ReplyList
import me.zsj.interessant.utils.CircleTransform

class ReplyAdapter(private val datas: List<ReplyList>, private val description: View) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            R.layout.item_movie_detail_header -> return SimpleViewHolder(description)
            R.layout.item_reply -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_reply, parent, false)
                return Holder(view)
            }
            else -> return SimpleViewHolder(description)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == R.layout.item_reply) {
            val reply = datas[position - 1]
            val viewHolder = holder as Holder
            Glide.with(viewHolder.avatar.context)
                .load(reply.user.avatar)
                .transform(CircleTransform(viewHolder.avatar.context))
                .into(viewHolder.avatar)

            viewHolder.replyAvatar.text = reply.user.nickname
            viewHolder.replyDesc.text = reply.message
            viewHolder.replyTime.text = DateUtils.getRelativeTimeSpanString(reply.createTime,
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString().toLowerCase()
            viewHolder.like.text = reply.likeCount.toString()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == DETAIL_DESCRIPTION_TYPE) {
            R.layout.item_movie_detail_header
        } else R.layout.item_reply
    }

    override fun getItemCount(): Int {
        var count = 1
        if (datas.size > 0) count += datas.size
        return count
    }

    internal class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var avatar: ImageView
        var replyAvatar: TextView
        var replyDesc: TextView
        var replyTime: TextView
        var like: TextView

        init {
            avatar = itemView.findViewById(R.id.movie_avatar) as ImageView
            replyAvatar = itemView.findViewById(R.id.reply_avatar) as TextView
            replyDesc = itemView.findViewById(R.id.replay_desc) as TextView
            replyTime = itemView.findViewById(R.id.reply_time) as TextView
            like = itemView.findViewById(R.id.likeCount) as TextView
        }
    }

    internal class SimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    companion object {

        private val DETAIL_DESCRIPTION_TYPE = 0
    }

}