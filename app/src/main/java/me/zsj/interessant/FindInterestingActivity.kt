package me.zsj.interessant

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.drakeet.multitype.Items
import me.drakeet.multitype.MultiTypeAdapter
import me.zsj.interessant.api.InterestingApi
import me.zsj.interessant.base.ToolbarActivity
import me.zsj.interessant.binder.related.Card
import me.zsj.interessant.binder.related.HeaderItem
import me.zsj.interessant.binder.related.RelatedHeader
import me.zsj.interessant.binder.video.FooterForward
import me.zsj.interessant.model.Category
import me.zsj.interessant.model.CategoryInfo
import me.zsj.interessant.model.ItemList
import me.zsj.interessant.model.SectionList

class FindInterestingActivity : ToolbarActivity() {

    lateinit var adapter: MultiTypeAdapter
    val items: Items = Items()
    private lateinit var interestingApi: InterestingApi
    private lateinit var categoryInfo: CategoryInfo
    private var id: Int = 0

    companion object {
        const val HORIZONTAL_SCROLL_CARD_SECTION = "horizontalScrollCardSection"
        const val VIDEO_LIST_SECTION = "videoListSection"
        const val AUTHOR_SECTION = "authorSection"

    }

    override fun providerLayoutId(): Int {
        return R.layout.find_interesting_activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val title = intent.getStringExtra(MainActivity.TITLE)
        ab!!.title = title.substring(1)
        id = intent.getIntExtra(MainActivity.CATEGORY_ID, 0)
        interestingApi = RetrofitFactory.getRetrofit().createApi(InterestingApi::class.java)
        val list = findViewById<RecyclerView>(R.id.list)
        adapter = MultiTypeAdapter(items)
        list.adapter = adapter
        Register.registerFindItem(adapter!!, this@FindInterestingActivity)
        findVideos()
    }

    private fun findVideos() {
        interestingApi.findVideo(id)
            .compose(bindToLifecycle())
            .filter({ find -> find != null })
            .filter({ find -> find.sectionList != null })
            .doOnNext({ find -> this.categoryInfo = find.categoryInfo })
            .flatMap({ find -> Flowable.fromIterable(find.sectionList) })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate({ adapter.notifyDataSetChanged() })
            .subscribe({it -> this.addData(it)})//, ErrorAction.error(this)
    }

    private fun addData(sectionList: SectionList) {
        if (sectionList.type == HORIZONTAL_SCROLL_CARD_SECTION) {
            val data = sectionList.itemList[0].data
            items.add(RelatedHeader(data.header, false))
            items.add(Card(sectionList.itemList[0]))
        } else if (sectionList.type == VIDEO_LIST_SECTION) {
            items.add(Category(sectionList.header.data.text))
            addVideo(sectionList.itemList)
            items.add(FooterForward(categoryInfo.id, sectionList.footer.data.text))
        } else if (sectionList.type == AUTHOR_SECTION) {
            addAuthorItem(sectionList.itemList)
        }
    }


    private fun addVideo(itemLists: List<ItemList>) {
        for (item in itemLists) {
            items.add(item)
        }
    }

    private fun addAuthorItem(itemLists: List<ItemList>) {
        for (item in itemLists) {
            items.add(HeaderItem(item.data.header, true))
            items.add(Card(item))
        }
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