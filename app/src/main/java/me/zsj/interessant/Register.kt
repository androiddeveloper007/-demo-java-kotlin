package me.zsj.interessant

import android.app.Activity
import me.drakeet.multitype.MultiTypeAdapter
import me.zsj.interessant.binder.daily.CategoryViewBinder
import me.zsj.interessant.binder.daily.DailyViewBinder
import me.zsj.interessant.binder.related.*
import me.zsj.interessant.binder.video.FooterForward
import me.zsj.interessant.binder.video.FooterForwardViewBinder
import me.zsj.interessant.binder.video.VideoViewBinder
import me.zsj.interessant.model.Category
import me.zsj.interessant.model.ItemList

class Register {
    companion object {

        fun registerItem(adapter: MultiTypeAdapter, context: Activity) {
            adapter.register(Category::class.java, CategoryViewBinder());
            adapter.register(ItemList::class.java, DailyViewBinder(context));
        }

        fun registerFindItem(adapter: MultiTypeAdapter, context: Activity) {
            adapter.register(FooterForward::class.java, FooterForwardViewBinder())
            adapter.register(Category::class.java, CategoryViewBinder())
            adapter.register(ItemList::class.java, VideoViewBinder(context))
            registerCommonItem(adapter, context)
        }

        fun registerCommonItem(adapter: MultiTypeAdapter, context: Activity) {
            adapter.register(HeaderItem::class.java, HeaderViewBinder())
            adapter.register(Card::class.java, CardViewBinder())
            adapter.register(RelatedHeader::class.java, RelatedHeaderViewBinder())
        }
    }
}