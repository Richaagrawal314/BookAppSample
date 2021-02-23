package com.internshala.bookhun.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
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
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment() {

    private val TAG = "MainActivity"
    private lateinit var mbookinput: EditText
    private lateinit var button: Button
    private lateinit var recyclerSearch: RecyclerView
    private lateinit var layoutlinear: RecyclerView.LayoutManager
    private lateinit var recycleadapter: DashboardRecyclerAdapter
    private lateinit var progressLayout: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        mbookinput = view.findViewById(R.id.txtBookInput)
        button = view.findViewById(R.id.btnfetch)
        recyclerSearch = view.findViewById((R.id.recycleSearch))
        progressLayout = view.findViewById(R.id.searchProgressBar)
        progressLayout.visibility = View.GONE

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        layoutlinear = LinearLayoutManager(activity)
        recyclerSearch.layoutManager = layoutlinear

        button.setOnClickListener {
            progressLayout.visibility = View.VISIBLE
            getLatestBooks()
        }

    }


    private fun getLatestBooks() {
        val bookList = arrayListOf<DataBook>()
        //set queryString to whatever type books you want on your Dashboard
        val queryString = mbookinput.text.toString()
        val queue: RequestQueue = Volley.newRequestQueue(context)
        val url =
            "https://www.googleapis.com/books/v1/volumes?q={$queryString}&maxResults=10&printType=books"
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
                        if(imgurl.isEmpty())
                        imgurl="xxx"
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
                recyclerSearch.adapter = recycleadapter
            },
            {
                Log.i("TAG", "VolleyError is : $it")
            })


        queue.add(stringRequest)

    }

}
