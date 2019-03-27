package com.tom.whisper.model

class Node(var whisper:Whisper){

    var hearts:Int = 0
    var pointTo:Node? = null
    var wid:String = ""
    var replies:Int = 0

    init {
        hearts = whisper.me2
        wid = whisper.wid
        replies = whisper.replies
    }

    override fun toString(): String {
        return "{ hearts:$hearts  wid:$wid replies:$replies pointTo:${pointTo?.wid}}"
    }
}

