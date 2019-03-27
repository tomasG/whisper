package com.tom.whisper.backend


import com.tom.whisper.model.PopularFeed
import com.tom.whisper.model.RepliesResponse
import com.tom.whisper.model.Whisper
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface BackendAPI {

    @GET("whispers/popular")
    fun getWhispers(@Query("limit") limit:Int): Call<PopularFeed>

    @GET("whispers/replies")
    fun getReply(@Query("limit") limit:Int,
                 @Query("wid") wid:String): Observable<RepliesResponse>

    @GET("whispers/replies")
    fun getReplyCall(@Query("limit") limit:Int,
                     @Query("wid") wid:String,
                     @Header("WID") WID:String): Call<RepliesResponse>

}