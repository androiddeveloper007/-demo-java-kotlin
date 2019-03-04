package me.zsj.interessant.api

import io.reactivex.Flowable
import me.zsj.interessant.model.Daily
import me.zsj.interessant.model.Find
import me.zsj.interessant.model.Interesting
import retrofit2.http.GET
import retrofit2.http.Query

interface InterestingApi {
    @GET("v3/videos?num=10")
    fun getInteresting(
        @Query("start") start: Int, @Query("categoryId") categoryId: Int,
        @Query("strategy") strategy: String
    ): Flowable<Interesting>

    @GET("v3/tag/videos")
    fun related(
        @Query("start") start: Int, @Query("tagId") id: Int,
        @Query("strategy") strategy: String
    ): Flowable<Interesting>

    @GET("v3/pgc/videos")
    fun relatedHeader(
        @Query("start") start: Int, @Query("pgcId") id: Int,
        @Query("strategy") strategy: String
    ): Flowable<Interesting>

    @GET("v3/categories/detail")
    fun findVideo(@Query("id") id: Int): Flowable<Find>

    @GET("v3/categories/videoList")
    fun videoList(
        @Query("id") id: Int, @Query("start") start: Int,
        @Query("strategy") strategy: String
    ): Flowable<Interesting>

}