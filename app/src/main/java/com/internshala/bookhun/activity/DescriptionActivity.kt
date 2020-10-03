package com.internshala.bookhun.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.internshala.bookhun.R
import com.internshala.bookhun.database.BDatabase
import com.internshala.bookhun.database.BookEntity
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import org.json.JSONObject

class DescriptionActivity
    : AppCompatActivity() {
    private val tAG = "Description_Activity"

    private lateinit var txtBookName: TextView
    private lateinit var imgBookDesc: ImageView
    private lateinit var txtBookauthor: TextView
    private lateinit var txtBookPrice: TextView
    private lateinit var txtBookDescription: TextView
    private lateinit var btnAddToFav: Button
    private lateinit var progressLayout: RelativeLayout
    private lateinit var progressBar: ProgressBar

    private var mbookinput: String? = "sampletext"

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description_activity)
        txtBookName = findViewById(R.id.txtdescbookname)
        txtBookauthor = findViewById(R.id.txtdescauthorname)
        txtBookPrice = findViewById(R.id.txtdescbookprice)
        imgBookDesc = findViewById(R.id.imgDesc)
        txtBookDescription = findViewById(R.id.txtDescDetails)
        btnAddToFav = findViewById(R.id.btnFavouriteDescription)
        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        val queue = Volley.newRequestQueue(this@DescriptionActivity)

        if (intent != null) {
            mbookinput = intent.getStringExtra("bookName")
        } else {
            finish()
            Toast.makeText(this@DescriptionActivity, "Some Error Occurred", Toast.LENGTH_SHORT)
                .show()
        }

        if (mbookinput == "sampletext") {
            finish()
            Toast.makeText(this@DescriptionActivity, "Some Error Occurred", Toast.LENGTH_SHORT)
                .show()
        }


        val queryString = mbookinput.toString()

        //Log.i(tAG, "click Listener :$queryString")
        val url = "https://www.googleapis.com/books/v1/volumes?q={$queryString}&maxResults=1"


        val stringRequest = StringRequest(
            Request.Method.GET, url, {
                //Log.i(TAG, "response is: $it")
                val jsonObject = JSONObject(it)

                val itemArray = jsonObject.getJSONArray("items")
                lateinit var bookId: String
                lateinit var imgurl: String

                progressLayout.visibility = View.GONE

                for (i in 0 until itemArray.length()) {
                    val book = itemArray.getJSONObject(i)
                    val volumeInfo = book.getJSONObject("volumeInfo")
                    bookId = book.getString("id")
                    try {
                        txtBookName.text = volumeInfo.getString("title")

                        if (volumeInfo.has("authors")) {
                            val authorArray = volumeInfo.getJSONArray("authors")
                            txtBookauthor.text = authorArray[0].toString()
                        } else
                            txtBookauthor.text = "Author Not Known"

                        if (volumeInfo.has("description"))
                            txtBookDescription.text = volumeInfo.getString("description")
                        else
                            txtBookDescription.text = "Description Not Found"

                        val price = book.getJSONObject("saleInfo")
                        txtBookPrice.text = if (price.getString("saleability") == "FOR_SALE") {
                            price.getJSONObject("listPrice").getDouble("amount").toString() +
                                    price.getJSONObject("listPrice").getString("currencyCode")
                        } else
                            "Not for Sale"
                        if (volumeInfo.has("imageLinks"))
                            imgurl = volumeInfo.getJSONObject("imageLinks")
                                .getString("thumbnail")

                    } catch (e: Exception) {
                        Log.e(tAG, "json-parse exception is: ${e.message}")
                        // e.printStackTrace()
                    }


                    Picasso.get().load(imgurl).placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_profile)
                        .into(imgBookDesc, object : Callback {
                            override fun onSuccess() {
                                Log.i(tAG, "on success callback")
                            }

                            override fun onError(e: Exception) {
                                Log.e(tAG, "onError callback ${e.message}")
                            }
                        })
                }
                val bookent = BookEntity(
                    bookId,
                    txtBookName.text.toString(),
                    txtBookauthor.text.toString(),
                    //  txtBookPrice.text.toString(),
                    txtBookDescription.text.toString(),
                    imgurl
                )

                val checkFav = DBAsyncTask(applicationContext, bookent, 1).execute().get()
                if (checkFav) {
                    btnAddToFav.text = "Remove from Favourites"
                    val favColor = ContextCompat.getColor(applicationContext, R.color.colorFav)
                    btnAddToFav.setBackgroundColor(favColor)
                } else {
                    btnAddToFav.text = "Add to Favourites"
                    val noFavColor =
                        ContextCompat.getColor(applicationContext, R.color.colorPrimary)
                    btnAddToFav.setBackgroundColor(noFavColor)
                }

                btnAddToFav.setOnClickListener {
                    if (!DBAsyncTask(applicationContext, bookent, 1).execute().get()) {
                        val result = DBAsyncTask(applicationContext, bookent, 2).execute().get()
                        if (result) {
                            Toast.makeText(
                                applicationContext,
                                "Added to Favourites",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            btnAddToFav.text = "Remove from Favourites"
                            val favColor =
                                ContextCompat.getColor(applicationContext, R.color.colorFav)
                            btnAddToFav.setBackgroundColor(favColor)
                        } else {
                            Log.i(tAG, "Error in Removefromfav")
                            /* Toast.makeText(applicationContext, "Some error occurred",
                                 Toast.LENGTH_SHORT).show()*/
                        }
                    } else {
                        val result = DBAsyncTask(applicationContext, bookent, 3).execute().get()
                        if (result) {
                            Toast.makeText(
                                applicationContext, "Removed from Favourites",
                                Toast.LENGTH_SHORT
                            ).show()
                            btnAddToFav.text = "Add to Favourites"
                            val noFavColor =
                                ContextCompat.getColor(applicationContext, R.color.colorPrimary)
                            btnAddToFav.setBackgroundColor(noFavColor)
                        } else {
                            Log.i(tAG, "Error in Addtofav")
                            /*Toast.makeText(applicationContext,
                                "Some error occurred", Toast.LENGTH_SHORT).show()*/
                        }
                    }
                }


            },
            {
                Log.e(tAG, "Volley Error $it")

                /*  Toast.makeText(this@DescriptionActivity, "some volley error occured",
                     Toast.LENGTH_SHORT) .show()*/
            })
        queue.add(stringRequest)
    }


    class DBAsyncTask(
        val context: Context,
        private val bookentity: BookEntity,
        private val mode: Int
    ) :
        AsyncTask<Void, Void, Boolean>() {

        private val db = Room.databaseBuilder(context, BDatabase::class.java, "books-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {

            when (mode) {
                1 -> {
                    val book: BookEntity? = db.bookDao().getBookById(bookentity.book_id)
                    db.close()
                    return book != null
                }
                2 -> {
                    db.bookDao().insertBook(bookentity)
                    db.close()
                    return true
                }
                3 -> {
                    db.bookDao().deleteBook(bookentity)
                    db.close()
                    return true
                }
            }
            return false
        }
    }
}