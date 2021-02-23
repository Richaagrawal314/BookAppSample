package com.internshala.bookhun.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.internshala.bookhun.R
import com.internshala.bookhun.activity.DescriptionActivity
import com.internshala.bookhun.database.BookEntity
import com.squareup.picasso.Picasso

class FavouriteRecyclerAdapter(val context: Context, private val bookList: List<BookEntity>) :
    RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favourite_single_card, parent, false)
        return FavouriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val book = bookList[position]

        holder.txtBookName.text = book.bookNameEnCl
        holder.txtBookAuthor.text = book.bookAuthorEnCl
      //  holder.txtBookPriceHolder.text = book.book_id
        Picasso.get().load(book.bookImageEnCl).into(holder.imgFavImage)

        holder.favouriteCard.setOnClickListener {
            val intent = Intent(context, DescriptionActivity::class.java)
            intent.putExtra("bookName", book.bookNameEnCl)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    class FavouriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtBookName: TextView = view.findViewById(R.id.txtFavTitle)
        val txtBookAuthor: TextView = view.findViewById(R.id.txtFavAuthor)
       // val txtBookPriceHolder: TextView = view.findViewById(R.id.txtFavPrice)
      //  val txtBookRating: TextView = view.findViewById(R.id.txtFavRating)
        val imgFavImage: ImageView = view.findViewById(R.id.imgFavBook)
       val favouriteCard: LinearLayout = view.findViewById(R.id.llFavLayout)
    }
}