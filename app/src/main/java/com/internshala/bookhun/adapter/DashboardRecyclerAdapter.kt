package com.internshala.bookhun.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.internshala.bookhun.R
import com.internshala.bookhun.activity.DescriptionActivity
import com.internshala.bookhun.model.DataBook
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class DashboardRecyclerAdapter(val context: Context, private val itemList: ArrayList<DataBook>) :
    RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>() {

    class DashboardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookName: TextView = itemView.findViewById((R.id.txtbookname))
        val author: TextView = itemView.findViewById((R.id.txtauthorname))
        val priceView: TextView = itemView.findViewById((R.id.txtprice))
        val ratingView: TextView = itemView.findViewById(R.id.txtbookrating)
        val bookImage: ImageView = itemView.findViewById(R.id.imgBookImage)
        val detailCard: LinearLayout = itemView.findViewById(R.id.detailCard)
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
        holder.bookName.text = book.dcBookName
        holder.author.text = book.dcBookAuthor
        holder.priceView.text = book.dcBookPrice
        holder.ratingView.text = book.dcBookRating
        Picasso.get()
            .load(book.dcBookImage).placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_foreground)
            .into(holder.bookImage, object : Callback {
                override fun onSuccess() {
                    Log.i("TAG", "on success callback")
                }

                override fun onError(e: Exception) {
                    Log.e("TAG", "onError callback ${e.message}")
                }
            })
        // holder.bookimage.setImageResource(book.dcBookImage)

        holder.detailCard.setOnClickListener {
            val intent = Intent(context, DescriptionActivity::class.java)
            intent.putExtra("bookName", book.dcBookName)
            context.startActivity(intent)
        }

    }
}