package me.zsj.interessant

import me.zsj.interessant.base.ToolbarActivity

class RelatedActivity: ToolbarActivity() {

    override fun providerLayoutId(): Int {
        return R.layout.related_activity
    }

    companion object {
        lateinit var ID: String
    }

}