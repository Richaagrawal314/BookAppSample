package com.internshala.bookhun.fragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.internshala.bookhun.R
import com.internshala.bookhun.adapter.DashboardRecyclerAdapter
import com.internshala.bookhun.model.DataBook
import com.internshala.bookhun.util.ConnectionManager
import org.json.JSONObject

class DashboardFragment : Fragment() {
    private lateinit var recyclerdash: RecyclerView
    private lateinit var layoutlinear: RecyclerView.LayoutManager
    private lateinit var checkbtn: Button
    private lateinit var recycleadapter: DashboardRecyclerAdapter
    private lateinit var progressLayout: RelativeLayout
    // private lateinit var progressBar: ProgressBar


    /*  private val bookInfoList = arrayListOf(
          DataBook(
              "A Thousand Splendid Dreams", "Khaled Hosseini", "ffdvf",
              "fgvgv", R.drawable.jellyfish
          ),
          DataBook(
              "Lolita", "Vladmir something", "566",
              "212", R.drawable.koala
          ),
      and 10 more {Initially this data was passed to adapter and displayed.}
      )*/


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        recyclerdash = view.findViewById((R.id.recyclerdash))
        checkbtn = view.findViewById(R.id.btncheckinternet)
        progressLayout = view.findViewById(R.id.dasProgressLayout)
        progressLayout.visibility = View.VISIBLE
        // progressBar = findViewById(R.id.progressBar)
        // progressBar.visibility = View.VISIBLE

        checkbtn.setOnClickListener {
            if (ConnectionManager().checkConnectivity(activity as Context)) {
                val dialog = AlertDialog.Builder(activity as Context)
                dialog.setTitle("Success")
                dialog.setMessage("Internet Connection Found")
                dialog.setPositiveButton("Ok") { _, _ -> }
                dialog.setNegativeButton("Cancel") { _, _ -> }
                dialog.create()
                dialog.show()
            } else {
                val dialog = AlertDialog.Builder(activity as Context)
                dialog.setTitle("Success")
                dialog.setMessage("Internet Connection Not Found")
                dialog.setPositiveButton("Ok") { _, _ -> }
                dialog.setNegativeButton("Cancel") { _, _ -> }
                dialog.create()
                dialog.show()
            }
        }

        layoutlinear = LinearLayoutManager(activity)
        recyclerdash.layoutManager = layoutlinear
        getLatestBooks()
        return view
    }

    private fun getLatestBooks() {
        val bookList = arrayListOf<DataBook>()
        //set queryString to whatever type books you want on your Dashboard
        val queryString = "best sellers books recent"
        val queue: RequestQueue = Volley.newRequestQueue(context)
        val url =
            "https://www.googleapis.com/books/v1/volumes?q={$queryString}&maxResults=40&printType=books"
        var title = ""
        var authors = ""
        var imgurl = ""
        var listPrice = ""
        var rating = ""
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            {
                //  Log.i("TAG", "response $it")
                val jsonObj = JSONObject(it)
                val itemArray = jsonObj.getJSONArray("items")
                progressLayout.visibility = View.GONE

                for (i in 0 until itemArray.length()) {
                    val book = itemArray.getJSONObject(i)
                    val volumeInfo = book.getJSONObject("volumeInfo")
                    try {
                        title = volumeInfo.getString("title")
                        authors = if (volumeInfo.has("authors")) {
                            val authorArray = volumeInfo.getJSONArray("authors")
                            authorArray[0].toString()
                        } else
                            "Author Not Known"


                        val price = book.getJSONObject("saleInfo")
                        listPrice = if (price.getString("saleability") == "FOR_SALE") {
                            price.getJSONObject("listPrice").getDouble("amount").toString() +
                                    price.getJSONObject("listPrice").getString("currencyCode")
                        } else
                            "Not for Sale"
                        //average rating is not available on most books, so I changed it to mature rating.
                        //will think something else later
                        rating = if (volumeInfo.getString("maturityRating") == "NOT_MATURE")
                            " _ "
                        else
                            "18+"

                        imgurl = volumeInfo.getJSONObject("imageLinks")
                            .getString("thumbnail")
                        //Log.i("TAG", "listPrice is: $listPrice")
                        //Log.i("TAG", "title is: $title")
                        //Log.i("TAG", "author is: $authors"
                        Log.i("TAG", "rating is $rating")

                    } catch (e: Exception) {
                        Log.e("TAG", "exception is: ${e.message}")
                    }

                    val dBook = DataBook(title, authors, listPrice, rating, imgurl)
                    bookList.add(dBook)

                }

                recycleadapter = DashboardRecyclerAdapter(activity as Context, bookList)
                recyclerdash.adapter = recycleadapter
            },
            {
                Log.i("TAG", "VolleyError is : $it")
            })


        queue.add(stringRequest)

    }


}


