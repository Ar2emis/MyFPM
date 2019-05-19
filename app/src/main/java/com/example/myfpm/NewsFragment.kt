package com.example.myfpm


import android.content.Intent
import android.os.Bundle
import android.view.*
import com.google.firebase.auth.FirebaseAuth
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.news.view.*
import java.util.*
import androidx.recyclerview.widget.RecyclerView.OnScrollListener as OnScrollListener


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class NewsFragment : androidx.fragment.app.Fragment() {

    var news: MutableList<News> = mutableListOf()
    lateinit var recyclerView: RecyclerView
    var newsHeight: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)

        recyclerView = recyclerView_news
        val adapter = GroupAdapter<ViewHolder>()
        recyclerView.adapter = adapter

        fetchNews()

        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if(!recyclerView.canScrollVertically(1)) {
                    fetchNews()
                    recyclerView.scrollTo(0, newsHeight)
                }
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            refreshNews()
        }

        add_news_button.setOnClickListener {
            val intent = Intent(this.context, New_news::class.java)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            R.id.singOut_butt -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this.context,LoginActivity::class.java)
                intent.flags =Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private var lastDoc: DocumentSnapshot? = null
    private val newsLimit: Long = 10

    fun fetchNews(){

        if(lastDoc == null){
            FirebaseFirestore.getInstance()
                .collection("news")
                .orderBy("date", Query.Direction.DESCENDING).limit(newsLimit).get()
                .addOnCompleteListener {

                    if(it.result!!.isEmpty) return@addOnCompleteListener

                    news.addAll(it.result!!.toObjects(News::class.java))
                    lastDoc = it.result!!.documents.last()

                    val adapter = GroupAdapter<ViewHolder>()

                    for(new in news){
                        val item = NewsItem(new)
                        adapter.add(item)
                    }

                    recyclerView.swapAdapter(adapter, false)
                }
        }
        else {
            FirebaseFirestore.getInstance()
                .collection("news")
                .orderBy("date", Query.Direction.DESCENDING).limit(newsLimit)
                .startAfter(lastDoc!!).get()
               .addOnCompleteListener {

                   if(it.result!!.isEmpty) return@addOnCompleteListener

                   val newsLastPosition = news.size - 1
                   news.addAll(it.result!!.toObjects(News::class.java))
                   lastDoc = it.result!!.documents.last()

                   val adapter = GroupAdapter<ViewHolder>()

                   var i = 0
                   var newsItem: NewsItem? = null

                   for(new in news){
                       val item = NewsItem(new)
                       adapter.add(item)

                       if(i == newsLastPosition)
                           newsItem = item
                       ++i
                   }

                   recyclerView.swapAdapter(adapter, true)

                   recyclerView.scrollToPosition(newsLastPosition)
                   recyclerView.scrollTo(0, newsItem!!.height)
               }
        }
    }

    private fun refreshNews() {
        swipeRefreshLayout.isRefreshing = true

        news = mutableListOf()
        lastDoc = null
        fetchNews()

        swipeRefreshLayout.isRefreshing = false
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.profmenu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}

class NewsItem(private val new: News): Item<ViewHolder>(){

    var height = 0

    override fun getLayout(): Int {
        return R.layout.news
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        initializeNew(viewHolder, position)
    }

    private fun initializeNew(viewHolder: ViewHolder, position: Int){
        FirebaseFirestore.getInstance().document("students/${new.creatorUid}")
            .get().addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                val userName = it.result!!.getString("name") +
                        " " + it.result!!.getString("surname")
                val date = "${new.date.hours}:" +
                        "${new.date.minutes}    " +
                        "${new.date.date}." +
                        "${new.date.month + 1}." +
                        "${new.date.year + 1900}"

                viewHolder.itemView.user_name_news_text_view.text = userName
                viewHolder.itemView.text_news_text_view.text = new.text
                viewHolder.itemView.date_news_text_view.text = date

                if(new.imageUrl != "null")
                Picasso.get().load(new.imageUrl)
                    .into(viewHolder.itemView.image_news_image_view)
                else
                    viewHolder.itemView.image_news_image_view.visibility = View.GONE
                Picasso.get().load(it.result!!.getString("imageUrl"))
                    .centerCrop().resize(128, 128)
                    .into(viewHolder.itemView.user_image_news_image_view)

                viewHolder.itemView.visibility = View.VISIBLE

                height = viewHolder.itemView.height
            }
    }
}

class News(val newsUUID: String, val creatorUid: String, val date: Date, val imageUrl: String,
           val text: String){
    constructor(): this("","",
        Date(1, 1, 1, 1, 1), "", "")
}

