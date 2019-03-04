package me.zsj.interessant

import android.os.Bundle
import me.zsj.interessant.base.ToolbarActivity

class SettingsActivity: ToolbarActivity() {

    override fun providerLayoutId(): Int {
        return R.layout.settings_activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar!!.setNavigationOnClickListener { finish() }
        fragmentManager.beginTransaction()
            .replace(R.id.container, SettingFragment())
            .commit()
    }
}
