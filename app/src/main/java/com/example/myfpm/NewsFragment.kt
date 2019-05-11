package com.example.myfpm


import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_news.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class NewsFragment : androidx.fragment.app.Fragment() {

    var news: MutableList<News> = mutableListOf()

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

        val adapter = GroupAdapter<ViewHolder>()

        recyclerView_news.adapter = adapter

        //adapter.add(NewsItem())
        //adapter.add(NewsItem())
        //adapter.add(NewsItem())

        fetchNews()

        add_news_button.setOnClickListener {
            val intent = Intent(this.context, New_news::class.java)
            startActivity(intent)
        }
    }

    private lateinit var lastDoc: DocumentSnapshot

    private fun fetchNews(){
        val newsLimit: Long = 10
        //if(!lastDoc.exists()){
            FirebaseFirestore.getInstance()
                .collection("news").limit(newsLimit).startAfter(lastDoc).get()
                .addOnCompleteListener {
                    if(!it.result!!.isEmpty) {
                        news.addAll(it.result!!.toObjects(News::class.java))
                        lastDoc = it.result!!.documents.last()
                        val adapter = GroupAdapter<ViewHolder>()
                        //adapter.addAll()
                    }
                }
        //}
        //else {
//            FirebaseFirestore.getInstance()
//                .collection("news").limit(newsLimit).startAfter().get()
//                .addOnCompleteListener {
//
//                }
        //}
    }

    private fun refreshNews() {
        swipeRefreshLayout.isRefreshing = true
        // Вот тут много кода с обновлением с бд
        swipeRefreshLayout.isRefreshing =false
    }
}

class NewsItem(new: News): Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.news
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

    }
}

class News(val name: String, val text: String, val image: String){
    constructor(): this("", "", "")
}

