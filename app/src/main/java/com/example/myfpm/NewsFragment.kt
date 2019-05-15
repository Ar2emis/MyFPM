package com.example.myfpm


import android.content.Intent
import android.os.Bundle
import android.view.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.news.view.*
import java.util.*


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
        setHasOptionsMenu(true)

        swipeRefreshLayout.setOnRefreshListener {
            refreshNews()
        }
        load_more_butt.visibility =View.INVISIBLE
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
    private val newsLimit: Long = 10

    private fun fetchNews(){
        if(lastDoc == null){
            FirebaseFirestore.getInstance()
                .collection("news").limit(newsLimit).get()
                .addOnCompleteListener {
                    if(!it.result!!.isEmpty) {
                        news.addAll(it.result!!.toObjects(News::class.java))
                        lastDoc = it.result!!.documents.last()
                        val adapter = GroupAdapter<ViewHolder>()

                        for(new in news){adapter.add(NewsItem(new))
                        }


                        recyclerView_news.adapter = adapter
                    }
                }
        }
        else {
            FirebaseFirestore.getInstance()
                .collection("news").limit(newsLimit).startAfter(lastDoc!!).get()
               .addOnCompleteListener {
                   if(!it.result!!.isEmpty) {
                       news.addAll(it.result!!.toObjects(News::class.java))
                       lastDoc = it.result!!.documents.last()
                       load_more_butt.visibility =View.VISIBLE
                       val adapter = GroupAdapter<ViewHolder>()
                       for(new in news){
                           adapter.add(NewsItem(new))
                       }

                       recyclerView_news.adapter = adapter
                   }
                }
        }
    }

    private fun refreshNews() {
        swipeRefreshLayout.isRefreshing = true
        // Вот тут много кода с обновлением с бд
        swipeRefreshLayout.isRefreshing =false
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.profmenu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}

class NewsItem(private val new: News): Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.news
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        FirebaseFirestore.getInstance().document("students/${new.creatorUid}")
            .get().addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                val userName = it.result!!.getString("name") +
                        " " + it.result!!.getString("surname")
                val date =
                    "${new.date.hours}:" +
                            "${new.date.minutes}  " +
                            "${new.date.date}." +
                            "${new.date.month + 1}." +
                            "${new.date.year + 1900}"

                viewHolder.itemView.user_name_news_text_view.text = userName
                viewHolder.itemView.text_news_text_view.text = new.text
                viewHolder.itemView.date_news_text_view.text = date

                Picasso.get().load(new.imageUrl)
                    .into(viewHolder.itemView.image_news_image_view)
                Picasso.get().load(it.result!!.getString("photoUrl"))
                    .fit().into(viewHolder.itemView.user_image_news_image_view)

                viewHolder.itemView.visibility = View.VISIBLE
            }
    }
}

class News(val creatorUid: String, val date: Date, val imageUrl: String,
           val text: String){
    constructor(): this("", Date(1, 1, 1, 1 ,1)
        , "", "")
}

