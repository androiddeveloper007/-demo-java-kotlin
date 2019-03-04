package me.zsj.interessant

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.HashMap
import java.util.concurrent.TimeUnit

@Suppress("UNCHECKED_CAST")
class WorkerRetrofit() {

    companion object {
        val BASE_URL = "http://baobab.kaiyanapp.com/api/"
    }

    internal var apis: MutableMap<Class<*>, Any> = HashMap()
    private var retrofit: Retrofit

    init {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(15, TimeUnit.SECONDS)
        builder.readTimeout(20, TimeUnit.SECONDS)
        retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    fun <T> createApi(service: Class<T>): T {
        if (!apis.containsKey(service)) {
            val instance = retrofit.create(service)
            apis.put(service, instance!!)
        }

        return apis[service] as T
    }

}