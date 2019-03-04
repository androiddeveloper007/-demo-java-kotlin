package me.zsj.interessant.base

import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import me.zsj.interessant.R
import me.zsj.interessant.SearchActivity
import org.jetbrains.anko.startActivity

abstract class ToolbarActivity : RxAppCompatActivity() {

    var toolbar: Toolbar? = null
    var ab: ActionBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(providerLayoutId())
        toolbar = findViewById(R.id.toolbar) as Toolbar
        if(toolbar!=null){
            setSupportActionBar(toolbar)
            ab = supportActionBar!!
            ab!!.setDisplayHomeAsUpEnabled(true)
        }
    }

    abstract fun providerLayoutId(): Int

    fun toSearch() {
        startActivity<SearchActivity>()
    }
}