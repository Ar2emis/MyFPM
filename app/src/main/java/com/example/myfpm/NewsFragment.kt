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
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import io.grpc.Context
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.news.view.*
import java.util.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class NewsFragment : androidx.fragment.app.Fragment() {

    var news: MutableList<News> = mutableListOf()
    lateinit var recyclerView: RecyclerView

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

        swipeRefreshLayout.setOnRefreshListener {
            refreshNews()
        }

        fetchNews()

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
    private val newsLimit: Long = 2

    fun fetchNews(){
        val newsSize = news.size

        if(lastDoc == null){
            FirebaseFirestore.getInstance()
                .collection("news").limit(newsLimit).get()
                .addOnCompleteListener {

                    if(it.result!!.isEmpty) return@addOnCompleteListener

                    news.addAll(it.result!!.toObjects(News::class.java))
                    lastDoc = it.result!!.documents.last()

                    Log.d("News", "!!! ${news[0].imageUrl}")

                    val adapter = GroupAdapter<ViewHolder>()

                    for(new in news.asReversed()){
                        adapter.add(NewsItem(new, news.size, this))
                    }

                    recyclerView.adapter = adapter
                }
        }
        else {
            FirebaseFirestore.getInstance()
                .collection("news").limit(newsLimit).startAfter(lastDoc!!).get()
               .addOnCompleteListener {

                   if(it.result!!.isEmpty) return@addOnCompleteListener

                   news.addAll(it.result!!.toObjects(News::class.java))
                   lastDoc = it.result!!.documents.last()

                   Log.d("News", "!!! ${news[0].imageUrl}")

                   val adapter = GroupAdapter<ViewHolder>()

                   for(new in news.asReversed()){
                       adapter.add(NewsItem(new,  news.size, this))
                   }

                   recyclerView.adapter = adapter
                   recyclerView.scrollToPosition(newsSize - 1)
                }
        }
    }

    private fun refreshNews() {
        swipeRefreshLayout.isRefreshing = true

        news = mutableListOf()
        lastDoc = null
        fetchNews()

        swipeRefreshLayout.isRefreshing =false
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.profmenu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}

class NewsItem(private val new: News, private val size: Int,
               private val fragment: NewsFragment): Item<ViewHolder>(){

    override fun getLayout(): Int {
        return R.layout.news
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        initializeNew(viewHolder, position, size)
    }

    private fun initializeNew(viewHolder: ViewHolder, position: Int, size: Int){
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

                Picasso.get().load(new.imageUrl)
                    .into(viewHolder.itemView.image_news_image_view)
                Picasso.get().load(it.result!!.getString("imageUrl"))
                    .fit().into(viewHolder.itemView.user_image_news_image_view)

                viewHolder.itemView.visibility = View.VISIBLE

                if(position == size - 1) {
                    viewHolder.itemView.load_more_new_text_view.visibility = View.VISIBLE
                    viewHolder.itemView.load_more_new_text_view.setOnClickListener {
                        fragment.fetchNews()
                        viewHolder.itemView.load_more_new_text_view.visibility = View.GONE
                    }
                }
                else {
                    viewHolder.itemView.load_more_new_text_view.visibility = View.GONE
                }
            }
    }
}

class News(val newUUID: String, val creatorUid: String, val date: Date, val imageUrl: String,
           val text: String){
    constructor(): this("","",
        Date(1, 1, 1, 1, 1), "", "")
}

