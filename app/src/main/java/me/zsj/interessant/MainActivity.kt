package me.zsj.interessant

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.content.ContextCompat.getColor
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import me.drakeet.multitype.Items
import me.drakeet.multitype.MultiTypeAdapter
import me.zsj.interessant.api.DailyApi
import me.zsj.interessant.base.ToolbarActivity
import me.zsj.interessant.model.Category
import me.zsj.interessant.model.Daily
import me.zsj.interessant.rx.ErrorAction
import me.zsj.interessant.rx.RxScroller
import org.jetbrains.anko.startActivity
import java.lang.Long

class MainActivity : ToolbarActivity() {

    lateinit var list: RecyclerView
    lateinit var refreshLayout: SwipeRefreshLayout
    lateinit var drawer: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var dailyApi: DailyApi
    val items = Items()
    private var dateTime = ""
    private lateinit var adapter: MultiTypeAdapter

    companion object {
        const val CATEGORY_ID = "categoryId"
        const val TITLE = "title"
        const val PROVIDER_ITEM = "item"
    }

    override fun providerLayoutId(): Int {
        return R.layout.main_activity
    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        list = findViewById(R.id.list) as RecyclerView
        refreshLayout = findViewById(R.id.refreshLayout) as SwipeRefreshLayout
        drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        ab!!.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
        drawer.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                // change the color to any color with transparency
                window.statusBarColor = getColor(
                    this@MainActivity, android.R.color.transparent
                )
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                window.statusBarColor = getColor(
                    this@MainActivity, R.color.colorPrimary
                )
            }
        })

        navigationView = findViewById(R.id.nav_view) as NavigationView
        setupDrawerContent(navigationView);

        dailyApi = RetrofitFactory.getRetrofit().createApi(DailyApi::class.java)
        setupRecyclerView()

        RxSwipeRefreshLayout.refreshes(refreshLayout)
            .compose(bindToLifecycle())
            .subscribe { loadData(true) }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        loadData(true)
    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener(object : NavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                item.setChecked(false)
                drawer.closeDrawers()
                findInteresting(item)
                return true
            }
        })
    }

    private fun findInteresting(item: MenuItem) {
        val id: Int
        val title: String
        when (item.itemId) {
            R.id.nav_cute_pet -> {
                id = 26
                title = resources.getString(R.string.cute_pet)
            }
            R.id.nav_funny -> {
                id = 28
                title = getResources().getString(R.string.funny)
            }
            R.id.nav_game -> {
                id = 30;
                title = getResources().getString(R.string.game);
            }
            R.id.nav_science -> {
                id = 32;
                title = getResources().getString(R.string.science);
            }
            R.id.nav_highlights -> {
                id = 34;
                title = getResources().getString(R.string.highlights);
            }
            R.id.nav_life -> {
                id = 34;
                title = getResources().getString(R.string.highlights);
            }
            R.id.nav_variety -> {
                id = 38;
                title = getResources().getString(R.string.variety);
            }
            R.id.nav_eating -> {
                id = 4;
                title = getResources().getString(R.string.eating);
            }
            R.id.nav_foreshow -> {
                id = 8;
                title = getResources().getString(R.string.foreshow);
            }
            R.id.nav_ad -> {
                id = 14;
                title = getResources().getString(R.string.advertisement);
            }
            R.id.nav_record -> {
                id = 22;
                title = getResources().getString(R.string.record);
            }
            R.id.nav_fashion -> {
                id = 24;
                title = getResources().getString(R.string.fashion);
            }
            R.id.nav_creative -> {
                id = 2;
                title = getResources().getString(R.string.creative);
            }
            R.id.nav_sports -> {
                id = 18;
                title = getResources().getString(R.string.sports);
            }
            R.id.nav_journey -> {
                id = 6;
                title = getResources().getString(R.string.journey);
            }
            R.id.nav_story -> {
                id = 12;
                title = getResources().getString(R.string.story);
            }
            R.id.nav_cartoon -> {
                id = 10;
                title = getResources().getString(R.string.cartoon);
            }
            R.id.nav_music -> {
                id = 20;
                title = getResources().getString(R.string.music);
            }
            R.id.nav_author -> {
                startActivity<VideoAuthorActivity>();
                return
            }
            R.id.settings -> {
                startActivity<SettingsActivity>();
                return
            }
            else -> return
        }
        val intent = Intent(this, FindInterestingActivity::class.java)
        intent.putExtra(CATEGORY_ID, id)
        intent.putExtra(TITLE, title)
        startActivity(intent)
    }

    @SuppressLint("CheckResult")
    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        val adapter = MultiTypeAdapter(items)
        Register.registerItem(adapter, this);
        list.layoutManager = layoutManager as RecyclerView.LayoutManager?;
        list.adapter = adapter;
        RxRecyclerView.scrollStateChanges(list)
            .compose(bindToLifecycle())
            .filter { !refreshLayout.isRefreshing }
            .compose(
                RxScroller.scrollTransformer(
                    layoutManager,
                    adapter, items
                )
            )
            .subscribe { newState ->
                run {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        loadData();
                    }
                }
            }

        list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    window.statusBarColor = getColor(
                        this@MainActivity, android.R.color.transparent
                    );
                } else {
                    window.statusBarColor = getColor(
                        this@MainActivity, R.color.colorPrimaryDark
                    );
                }
            }
        });
    }

    @SuppressLint("CheckResult")
    private fun loadData(clear: Boolean) {
        val result: Flowable<Daily>
        if (clear)
            result = dailyApi.daily
        else
            result = dailyApi.getDaily(Long.decode(dateTime))
        result.compose(bindToLifecycle())
            .filter { true }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                run {
                    if (clear) items.clear();
                }
            }
            .doAfterTerminate {
                refreshLayout.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }
            .subscribe(Consumer<Daily> { this.addData(it) }, ErrorAction.error(this))
    }

    private fun loadData() {
        loadData(false)
    }

    private fun addData(daily: Daily) {
        for (issueList in daily.issueList) {
            val date = issueList.itemList[1].data.text
            items.add(Category(date ?: "Today"))
            items.addAll(issueList.itemList)
        }
        val nextPageUrl = daily.nextPageUrl
        dateTime = nextPageUrl.substring(
            nextPageUrl.indexOf("=") + 1,
            nextPageUrl.indexOf("&")
        )
        adapter.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            drawer.openDrawer(GravityCompat.START);
            return true;
        } else if (item.itemId == R.id.search_action) {
            toSearch()
        } else if (item.itemId == R.id.action_settings) {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        return super.onOptionsItemSelected(item);
    }
}
