package me.zsj.interessant.api

import io.reactivex.Flowable
import me.zsj.interessant.model.Replies
import retrofit2.http.GET
import retrofit2.http.Query

interface ReplyApi {

    @GET("v1/replies/video")
    fun fetchReplies(@Query("id") id: Int): Flowable<Replies>

    @GET("v1/replies/video?num=10")
    fun fetchReplies(@Query("id") id: Int, @Query("lastId") lastId: Int): Flowable<Replies>

}