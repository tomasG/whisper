package com.tom.whisper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.squareup.picasso.Picasso
import com.tom.whisper.model.Whisper
import com.tom.whisper.model.WhisperSelector

class WhisperAdapter(var context: Context): androidx.recyclerview.widget.RecyclerView.Adapter<WhisperAdapter.WhisperViewHolder>() {

    var list:List<Whisper> = listOf()
        set(value){
            field = value
            notifyDataSetChanged()
        }
    var whisperSelectorListener:WhisperSelector? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WhisperViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.whisper_row,
            parent,false)
        return WhisperViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    fun getItem(position:Int):Whisper = list[position]

    override fun onBindViewHolder(holder: WhisperViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class WhisperViewHolder(view:View)  : androidx.recyclerview.widget.RecyclerView.ViewHolder(view),View.OnClickListener{

        @BindView(R.id.whisper_text)
        lateinit var text:TextView

        @BindView(R.id.whisper_hearts)
        lateinit var hearts:TextView

        @BindView(R.id.image)
        lateinit var image:ImageView

        init {
            ButterKnife.bind(this, view)
        }

        fun bind(whisper : Whisper){
            text.text = whisper.text
            hearts.text = whisper.me2.toString()
            Picasso.get().load(whisper.url).resize(50,50).centerCrop().into(image)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            var whisper = getItem(adapterPosition)
            whisperSelectorListener?.onSelected(whisper)
        }
    }
}