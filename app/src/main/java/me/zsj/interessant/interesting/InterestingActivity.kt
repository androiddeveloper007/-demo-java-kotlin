package me.zsj.interessant.interesting

import me.zsj.interessant.R
import me.zsj.interessant.base.ToolbarActivity

class InterestingActivity: ToolbarActivity() {
    override fun providerLayoutId(): Int {
        return R.layout.interesting_activity
    }



    companion object {
        const val RELATED_VIDEO = "related"
        const val RELATED_HEADER_VIDEO = "related_header"

        private var categoryId: Int = 0
        private var related: Boolean = false
        private var relatedHeader: Boolean = false
    }
}