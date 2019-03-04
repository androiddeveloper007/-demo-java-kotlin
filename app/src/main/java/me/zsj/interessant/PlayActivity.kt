package me.zsj.interessant

import android.graphics.PixelFormat
import android.media.AudioManager
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.*
import com.danikula.videocache.CacheListener
import com.danikula.videocache.HttpProxyCacheServer
import me.zsj.interessant.base.ToolbarActivity
import me.zsj.interessant.model.ItemList
import me.zsj.interessant.utils.NetUtils
import me.zsj.interessant.utils.PreferenceManager
import me.zsj.interessant.widget.VideoController
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import java.io.File
import java.io.IOException

class PlayActivity : ToolbarActivity(), SurfaceHolder.Callback, IMediaPlayer.OnBufferingUpdateListener,
    IMediaPlayer.OnCompletionListener, IMediaPlayer.OnPreparedListener,
    IMediaPlayer.OnInfoListener, IMediaPlayer.OnVideoSizeChangedListener, CacheListener {
    companion object {
        internal var FLAG_HIDE_SYSTEM_UI = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }

    private var surfaceHolder: SurfaceHolder? = null
    private lateinit var videoController: VideoController
    private var mediaPlayer: IjkMediaPlayer? = null
    private var item: ItemList? = null
    private var surfaceCreated = false

    override fun providerLayoutId(): Int {
        return R.layout.play_activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.decorView.systemUiVisibility = FLAG_HIDE_SYSTEM_UI
        videoController = findViewById<View>(R.id.video_controller) as VideoController
        val surfaceView = findViewById<View>(R.id.surface_view) as SurfaceView
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        item = intent.getParcelableExtra("item")
        val playUrl = item!!.data.playUrl
        assert(supportActionBar != null)
        supportActionBar!!.setTitle(item!!.data.title)
        surfaceHolder = surfaceView.holder
        surfaceHolder!!.addCallback(this)
        surfaceHolder!!.setFormat(PixelFormat.RGBA_8888)
        playVideo(playUrl)
    }

    private fun playVideo(path: String?) {
        try {
            mediaPlayer = IjkMediaPlayer()
            videoController.attachPlayer(mediaPlayer, item)
            val cacheWithWifi = PreferenceManager.getBooleanValue(this, SettingFragment.CACHE_KEY, true)
            if (cacheWithWifi && NetUtils.isWifiConnected(this)) {
                val proxyPath = cacheServer().getProxyUrl(path)
                cacheServer().registerCacheListener(this, path)
                mediaPlayer!!.dataSource = proxyPath
            } else {
                mediaPlayer!!.dataSource = path
            }
            mediaPlayer!!.prepareAsync()
            mediaPlayer!!.setOnBufferingUpdateListener(this)
            mediaPlayer!!.setOnCompletionListener(this)
            mediaPlayer!!.setOnPreparedListener(this)
            mediaPlayer!!.setOnInfoListener(this)
            mediaPlayer!!.setOnVideoSizeChangedListener(this)
            volumeControlStream = AudioManager.STREAM_MUSIC
            mediaPlayer!!.setScreenOnWhilePlaying(true)
            videoController.startVideoPlayback(surfaceHolder)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!mediaPlayer!!.isPlaying) {
            videoController.start()
        }
    }

    override fun onPause() {
        super.onPause()
        if (mediaPlayer!!.isPlaying) {
            videoController.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        videoController.surfaceDestory()
        cacheServer().unregisterCacheListener(this)
        release()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun release() {
        if (mediaPlayer != null) {
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }

    private fun cacheServer(): HttpProxyCacheServer {
        return HttpProxyCacheServer.Builder(this)
            .maxCacheSize((1024 * 1024 * 1024).toLong())
            .build()
    }

    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
        videoController.calculateVideoWidthAndHeight()
        videoController.startVideoPlayback(surfaceHolder)
    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {
        videoController.surfaceDestory()
    }

    override fun surfaceCreated(p0: SurfaceHolder?) {
        if (!surfaceCreated) {
            videoController.surfaceCreated()
            surfaceCreated = true
        }
    }

    override fun onBufferingUpdate(p0: IMediaPlayer?, p1: Int) {
        if (cacheServer().isCached(item!!.data.playUrl)) {
            videoController.onBufferUpdate(item!!.data.duration.toInt())
        }
    }

    override fun onCompletion(p0: IMediaPlayer?) {
        videoController.onCompleted()
    }

    override fun onPrepared(p0: IMediaPlayer?) {
        videoController.onPrepared(surfaceHolder)
    }

    override fun onInfo(iMediaPlaer: IMediaPlayer?, what: Int, extra: Int): Boolean {
        videoController.onInfo(what)
        return true
    }

    override fun onVideoSizeChanged(p0: IMediaPlayer?, width: Int, height: Int, p3: Int, p4: Int) {
        videoController.onVideoChanged(surfaceHolder, width, height)
    }

    override fun onCacheAvailable(cacheFile: File?, url: String?, percentsAvailable: Int) {
        var percentsAvailable1 = percentsAvailable
        percentsAvailable1 *= ((item!!.data.duration / 100).toInt())
        videoController.onBufferUpdate(percentsAvailable1)
    }
}