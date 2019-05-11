package com.example.myfpm


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_news.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class NewsFragment : androidx.fragment.app.Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        swipeRefreshLayout.setOnRefreshListener {
            refreshNews()
        }

        //recyclerView_news.layoutManager = LinearLayoutManager(this.context)
        recyclerView_news.adapter = NewsAdapter()

        add_news_button.setOnClickListener {
            val intent = Intent(this.context, New_news::class.java)
            startActivity(intent)
        }
    }

    private fun refreshNews() {
        swipeRefreshLayout.isRefreshing = true
        // Вот тут много кода с обновлением с бд
        swipeRefreshLayout.isRefreshing =false
    }
}

