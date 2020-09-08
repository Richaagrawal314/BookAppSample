package com.internshala.bookhun.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.internshala.bookhun.R
import com.internshala.bookhun.activity.DescriptionActivity
import com.internshala.bookhun.model.DataBook

class DashboardRecyclerAdapter(val context: Context, private val itemList: ArrayList<DataBook>) :
    RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>() {

    class DashboardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookView: TextView = itemView.findViewById((R.id.txtbookname))
        val authView: TextView = itemView.findViewById((R.id.txtauthorname))
        val priceView: TextView = itemView.findViewById((R.id.txtprice))
        val ratingView: TextView = itemView.findViewById(R.id.txtbookrating)
        val bookimage: ImageView = itemView.findViewById(R.id.imgBookImage)
        //val authv:TextView=
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerdash_singlerow, parent, false)

        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val book = itemList[position]
        holder.bookView.text = book.txtBookName
        holder.authView.text = book.txtBookAuthor
        holder.priceView.text = book.txtBookPrice
        holder.ratingView.text = book.txtBookRating
        holder.bookimage.setImageResource(book.imgBookImage)

        holder.bookView.setOnClickListener {
            val intent = Intent(context, DescriptionActivity::class.java)
            intent.putExtra("bookName", book.txtBookName)
            context.startActivity(intent)
        }

    }
}