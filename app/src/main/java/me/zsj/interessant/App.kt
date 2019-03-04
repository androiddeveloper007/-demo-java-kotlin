package me.zsj.interessant

import android.app.Application
import android.content.Context

import com.danikula.videocache.HttpProxyCacheServer

/**
 * @author zsj
 */

class App : Application() {

    lateinit var cacheServer: HttpProxyCacheServer

    private fun newCacheServer(): HttpProxyCacheServer {
        return HttpProxyCacheServer.Builder(this)
                .maxCacheSize((1024 * 1024 * 1024).toLong())
                .build()
    }

    companion object {
        fun cacheServer(context: Context): HttpProxyCacheServer {
            val app = context.applicationContext as App

            if (app.cacheServer == null)
                app.cacheServer = app.newCacheServer()

            return app.cacheServer
        }
    }

}
