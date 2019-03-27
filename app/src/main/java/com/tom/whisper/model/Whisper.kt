package com.tom.whisper.model

data class Whisper(var wid:String = "", var url:String = "",
                   var text:String = "", var me2:Int = 0,
                   var replies:Int = 0)

interface WhisperSelector {
    fun onSelected(whisper: Whisper)
}