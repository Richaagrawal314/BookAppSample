package com.internshala.bookhun.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.internshala.bookhun.R
import com.internshala.bookhun.adapter.FavouriteRecyclerAdapter
import com.internshala.bookhun.database.BDatabase
import com.internshala.bookhun.database.BookEntity

/**
 * A simple [Fragment] subclass.
 */
class FavFragment : Fragment() {
    private lateinit var recyclerFavourite: RecyclerView
    private lateinit var progresslayout: RelativeLayout
    private var progressBar: ProgressBar? = null
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var recyclerAdapter: FavouriteRecyclerAdapter
    private var dbBookList = listOf<BookEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourite, container, false)

        recyclerFavourite = view.findViewById(R.id.recyclerFavourite)
        progressBar = view.findViewById(R.id.progressBarFav)
        progresslayout = view.findViewById(R.id.progressLayoutFav)

        layoutManager = GridLayoutManager(activity as Context, 2)

        dbBookList = RetrieveFav(activity as Context).execute().get()

        if (activity != null) {
            progresslayout.visibility = View.GONE
            recyclerAdapter = FavouriteRecyclerAdapter(activity as Context, dbBookList)
            recyclerFavourite.adapter = recyclerAdapter
            recyclerFavourite.layoutManager = layoutManager
        }

        return view
    }


    class RetrieveFav(val context: Context) : AsyncTask<Void, Void, List<BookEntity>>() {
        override fun doInBackground(vararg params: Void?): List<BookEntity> {
            val db = Room.databaseBuilder(context, BDatabase::class.java, "books-db")
                .build()
            return db.bookDao().getAllBooks()
        }
    }
}
