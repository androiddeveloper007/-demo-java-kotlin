package me.zsj.interessant

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.zsj.interessant.api.SearchApi
import me.zsj.interessant.base.ToolbarActivity
import me.zsj.interessant.interesting.InterestingAdapter
import me.zsj.interessant.model.ItemList
import me.zsj.interessant.model.SearchResult
import me.zsj.interessant.rx.RxScroller
import java.util.*

class ResultActivity: ToolbarActivity() {

    private var start: Int = 0
    private val itemLists = ArrayList<ItemList>()
    private lateinit var searchApi: SearchApi
    private lateinit var adapter: InterestingAdapter

    override fun providerLayoutId(): Int {
        return R.layout.search_result_activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val keyword = intent.getStringExtra(SearchActivity.KEYWORD)
        assert(supportActionBar != null)
        supportActionBar!!.setTitle(keyword)

        searchApi = RetrofitFactory.getRetrofit().createApi(SearchApi::class.java)
        adapter = InterestingAdapter(this, itemLists)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        fetchResult(keyword)
        RxRecyclerView.scrollStateChanges(recyclerView)
            .compose(bindToLifecycle())
            .compose(
                RxScroller.scrollTransformer(
                    layoutManager,
                    adapter, itemLists
                )
            )
            .subscribe { newState ->
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    start += 10
                    fetchResult(keyword)
                }
            }
    }

    private fun fetchResult(keyword: String) {
        searchApi.query(keyword, start)
            .compose<SearchResult>(bindToLifecycle<SearchResult>())
            .filter { searchResult -> searchResult != null }
            .map<List<ItemList>> { searchResult -> searchResult.itemList }
            .doOnNext { itemList -> itemLists.addAll(itemList) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ itemLists -> adapter.notifyDataSetChanged() })//, ErrorAction.error(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.search_action) {
            toSearch()
        }
        return super.onOptionsItemSelected(item)
    }
}