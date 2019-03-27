package com.tom.whisper.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Log
import com.tom.whisper.backend.BackendAPI
import com.tom.whisper.model.PopularFeed
import com.tom.whisper.backend.RestAPIFactory
import com.tom.whisper.model.RepliesResponse
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WhisperViewModel : ViewModel(){

    var whispers: MutableLiveData<PopularFeed> = MutableLiveData()
    var replies: MutableLiveData<RepliesResponse> = MutableLiveData()
    private var backEnd:BackendAPI? = RestAPIFactory.generateRetrofitClient()

    companion object {
        const val LIMIT:Int = 200
    }

    fun getWhispers(): LiveData<PopularFeed> {
        if(whispers.value == null){
            loadWhispers()
        }
        return whispers
    }

    fun loadWhispers(){
        var call = backEnd?.getWhispers(LIMIT)
        var callback:Callback<PopularFeed> = object:Callback<PopularFeed> {
            override fun onFailure(call: Call<PopularFeed>, t: Throwable) {
                Log.e("Error->", t.message)
            }

            override fun onResponse(call: Call<PopularFeed>, response: Response<PopularFeed>) {
                if (response.isSuccessful) {
                    var popularFeed: PopularFeed? = response.body()
                    whispers.value = popularFeed
                }
            }
        }
        call?.enqueue(callback)
    }

}
