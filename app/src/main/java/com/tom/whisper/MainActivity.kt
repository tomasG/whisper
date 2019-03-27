package com.tom.whisper

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import butterknife.BindView
import butterknife.ButterKnife
import com.tom.whisper.model.PopularFeed
import com.tom.whisper.model.TreeSync
import com.tom.whisper.model.Whisper
import com.tom.whisper.model.WhisperSelector
import com.tom.whisper.viewmodel.WhisperViewModel

interface WhisperActions{
    fun showRelevantWhispering(list:List<Whisper>)
}

class MainActivity : AppCompatActivity(), WhisperSelector, WhisperActions {

    lateinit var whisperViewModel: WhisperViewModel
    lateinit var whisperAdapter: WhisperAdapter

    @BindView(R.id.whisper_recycler)
    lateinit var recycler: androidx.recyclerview.widget.RecyclerView

    @BindView(R.id.progressBar)
    lateinit var progressBar: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this, this)
        initComponents()
        observeActions()
    }

    private fun initComponents(){

        whisperViewModel =  ViewModelProviders.of(this).get(WhisperViewModel::class.java)
        whisperAdapter = WhisperAdapter(this)
        whisperAdapter.whisperSelectorListener = this
        recycler.adapter = whisperAdapter
        recycler.setHasFixedSize(true)
        recycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recycler.addItemDecoration(
            androidx.recyclerview.widget.DividerItemDecoration(
                this,
                androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun observeActions(){
        whisperViewModel.getWhispers().observe(this, Observer<PopularFeed> { popularFeed ->
            whisperAdapter.list = popularFeed?.popular ?: emptyList()
        })
    }

    override fun onSelected(whisper: Whisper) {
        buildTree(whisper)
        whisperAdapter.whisperSelectorListener = null

    }

    private fun buildTree(whisper:Whisper){
        progressBar.visibility = View.VISIBLE
        var tree:TreeSync? = TreeSync(whisper)
        tree?.listener = this
    }

    override fun onBackPressed() {
        whisperAdapter.whisperSelectorListener = this
        whisperViewModel.loadWhispers()
    }

    override fun showRelevantWhispering(list:List<Whisper>) {
        whisperAdapter.list = list
        progressBar.visibility = View.GONE
    }
}
