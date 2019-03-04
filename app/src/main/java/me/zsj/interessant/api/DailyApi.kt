package me.zsj.interessant.api

import io.reactivex.Flowable
import me.zsj.interessant.model.Daily
import retrofit2.http.GET
import retrofit2.http.Query

interface DailyApi {

    @get:GET("v2/feed?num=2")
    val daily: Flowable<Daily>

    @GET("v2/feed?num=2")
    fun getDaily(@Query("date") date: Long): Flowable<Daily>

}