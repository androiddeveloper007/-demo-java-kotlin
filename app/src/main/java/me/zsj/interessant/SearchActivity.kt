package me.zsj.interessant

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.zsj.interessant.api.SearchApi
import me.zsj.interessant.base.ToolbarActivity
import java.util.*

class SearchActivity: ToolbarActivity(),View.OnClickListener {
    override fun onClick(p0: View?) {
        searchEdit.text = null
    }

    companion object {
        const val KEYWORD = "keyword"
    }
    private lateinit var tagLayout: TagFlowLayout
    private lateinit var searchEdit: EditText
    private val tags = ArrayList<String>()

    override fun providerLayoutId(): Int {
        return R.layout.search_activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tagLayout = findViewById<View>(R.id.tag_layout) as TagFlowLayout
        searchEdit = findViewById<View>(R.id.search_edit) as EditText
        val clearButton = findViewById<View>(R.id.clear_btn) as ImageButton
        clearButton.setOnClickListener(this@SearchActivity)
        loadTrendingTag()
        keyListener()
    }

    private fun loadTrendingTag() {
        val trendingApi = RetrofitFactory.getRetrofit()
            .createApi(SearchApi::class.java!!)

        trendingApi.getTrendingTag()
            .compose(bindToLifecycle())
            .filter({ data -> data != null })
            .doOnNext({ data -> tags.addAll(data) })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ bindData() })//, ErrorAction.error(this)
    }

    private fun bindData() {
        tagLayout.setAdapter(object : TagAdapter<String>(tags) {
            override fun getView(parent: FlowLayout, position: Int, tagName: String): View {
                val tag = LayoutInflater.from(this@SearchActivity)
                    .inflate(R.layout.item_tag, parent, false) as TextView
                tag.text = tagName
                return tag
            }
        })

        tagLayout.setOnTagClickListener { view, position, parent ->
            startResultActivity(tags[position])
            true
        }
    }

    private fun keyListener() {
        searchEdit.setOnKeyListener { v, keyCode, event ->
            //onKey will call twice, First: ACTION_DOWN, Second:ACTION_UP
            if (event.action != KeyEvent.ACTION_DOWN) {
                true
            }
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                if (searchEdit.text.toString().isEmpty()) {
                    Toast.makeText(this@SearchActivity, "Keyword must not empty!",Toast.LENGTH_LONG).show()
                } else {
                    search(searchEdit.text.toString())
                }
            } else if (keyCode == KeyEvent.KEYCODE_BACK) {
                finish()
            }
            false
        }
    }

    private fun search(key: String) {
        startResultActivity(key)
    }

    private fun startResultActivity(keyword: String) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(KEYWORD, keyword)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }
}