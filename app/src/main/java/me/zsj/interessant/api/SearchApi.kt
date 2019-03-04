package me.zsj.interessant.api

import io.reactivex.Flowable
import me.zsj.interessant.model.SearchResult
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("v3/queries/hot")
    fun getTrendingTag(): Flowable<List<String>>

    @GET("v1/search?num=10")
    fun query(@Query("query") key: String, @Query("start") start: Int): Flowable<SearchResult>
}