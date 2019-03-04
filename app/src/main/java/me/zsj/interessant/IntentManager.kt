package me.zsj.interessant

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.util.Pair
import android.view.View

import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

import me.zsj.interessant.model.ItemList

import me.zsj.interessant.MainActivity.Companion.PROVIDER_ITEM

/**
 * @author zsj
 */

object IntentManager {

    fun flyToMovieDetail(context: Activity,
                         item: ItemList, view: View) {
        Picasso.with(context).load(item.data.cover.detail)
                .fetch(object : Callback {
                    override fun onSuccess() {
                        val intent = Intent(context, MovieDetailActivity::class.java)
                        intent.putExtra(PROVIDER_ITEM, item)
                        val options = ActivityOptions.makeSceneTransitionAnimation(
                                context,
                                Pair.create(view, context.getString(R.string.transition_shot)),
                                Pair.create(view, context.getString(R.string.transition_shot_background))
                        )
                        context.startActivity(intent, options.toBundle())
                    }

                    override fun onError() {
                        val intent = Intent(context, MovieDetailActivity::class.java)
                        intent.putExtra(PROVIDER_ITEM, item)
                        context.startActivity(intent)
                    }
                })
    }
}
