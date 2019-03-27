package com.tom.whisper.model

import android.util.Log
import com.tom.whisper.WhisperActions
import com.tom.whisper.backend.RestAPIFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TreeSync(var root: Whisper) {

    val map = mutableMapOf<String, List<Whisper>>()
    val control: MutableList<String> = ArrayList()
    val total = mutableMapOf<String, Int>()
    var listener: WhisperActions? = null

    init {
        map[root.wid] = listOf(root)
        Log.d("START REPLIES---->","")
        if (root.replies > 0) {
            getwhispers(root)
        }

    }



    fun getwhispers(whisperParent: Whisper) {
        var callback: Callback<RepliesResponse> = object : Callback<RepliesResponse> {
            override fun onFailure(call: Call<RepliesResponse>, t: Throwable) {
                Log.e("Error->", t.message)
            }

            override fun onResponse(call: Call<RepliesResponse>, response: Response<RepliesResponse>) {
                if (response.isSuccessful) {
                    var repliesResponse: RepliesResponse? = response.body()
                    if (response.isSuccessful) {
                        if (repliesResponse != null && repliesResponse.replies.isNotEmpty()) {
                            var widCall = call.request().url().queryParameter("wid") ?: ""
                            updateMap(widCall, repliesResponse.replies)
                            for (whisper in repliesResponse.replies) {
                                if (whisper.replies > 0) getwhispers(whisper)
                            }
                            control.remove(whisperParent.wid)
                            hasFinished()
                        }
                    }
                }
            }
        }
        control.add(whisperParent.wid)
        RestAPIFactory.generateRetrofitClient()
            ?.getReplyCall(whisperParent.replies, whisperParent.wid, whisperParent.wid)?.enqueue(callback)
    }

    @Synchronized
    fun hasFinished() {
        if (control.isEmpty()) {
            Log.d("END REPLIES---->","")
            calculatePath()
        }
    }

    /**
     * Function used to know what is the deepest leaf with max hearts
     */
    fun calculateMaxPath(): String {
        var finalMax: String = ""
        val max: Int = total.maxBy { it.value }?.value ?: 0
        val totalMap: Map<String, Int> = total.filter { it.value == max }
        val listKeys: ArrayList<String> = ArrayList(totalMap.keys)
        if (listKeys.size > 1) {
            for (key in listKeys) {
                //This is the deepest leaf
                if (!map.containsKey(key)) {
                    finalMax = key
                    break
                }
            }
            //If all are not final leaves, take anyone
            if (finalMax == "") {
                finalMax = listKeys[0]
            }
        } else {
            finalMax = listKeys.get(0)
        }
        return finalMax
    }

    fun calculatePath() {
        Log.d("START PATH---->","")
        getChilds(root, 0)
        val max = calculateMaxPath()
        val path = mutableListOf<Whisper>()
        if (max != root.wid) {
            searchInList(max, path)
        }
        path.reverse()
        listener?.showRelevantWhispering(path)
        Log.d("END PATH---->","")

    }

    fun getChilds(whisper: Whisper, hearts: Int) {
        val sum = hearts + whisper.me2
        if (whisper.wid != root.wid) {
            total[whisper.wid] = sum
        }
        if (whisper.replies > 0) {
            if (map.containsKey(whisper.wid)) {
                val list: List<Whisper> = map[whisper.wid] ?: emptyList()
                if (list.isNotEmpty()) {
                    for (whisper in list) {
                        getChilds(whisper, sum)
                    }
                }
            }
        }
    }

    fun findWhisper(list: List<Whisper>?, wid: String): Whisper? = list?.firstOrNull { it.wid == wid }


    fun searchInList(widSearched: String, path: MutableList<Whisper>) {
        var whisper: Whisper? = null
        for ((wid, list) in map) {
            whisper = findWhisper(list, widSearched)
            if (whisper != null) {
                path.add(whisper)
                if (whisper != root) {
                    searchInList(wid, path)
                }
                break;
            }
        }
    }


    @Synchronized
    fun updateMap(widCall: String, list: List<Whisper>?) {
        if (!list.isNullOrEmpty()) {
            map[widCall] = list
        }
    }
}

