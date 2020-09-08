package com.internshala.bookhun.fragment

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.internshala.bookhun.R
import com.internshala.bookhun.adapter.DashboardRecyclerAdapter
import com.internshala.bookhun.model.DataBook
import com.internshala.bookhun.util.ConnectionManager

class DashboardFragment : Fragment() {
    private lateinit var recyclerdash: RecyclerView
    private lateinit var layoutlinear: RecyclerView.LayoutManager
    private lateinit var checkbtn: Button
    private lateinit var recycleadapter: DashboardRecyclerAdapter

    private val bookInfoList = arrayListOf(
        DataBook(
            "War and Peace",
            "Someone I don't know. but I will find out about him. you chill",
            "550",
            "4.8",
            R.drawable.chrysanthemum
        ),
        DataBook(
            "Crime and Punishment ", "another language", "452", "2.5", R.drawable.desert
        ),
        DataBook(
            "God of Small Things", "Arundhoti Roy", "256", "4.8", R.drawable.hydrangeas
        ),
        DataBook(
            "A Thousand Splendid Dreams", "Khaled Hosseini", "ffdvf",
            "fgvgv", R.drawable.jellyfish
        ),
        DataBook(
            "Lolita", "Vladmir something", "566",
            "212", R.drawable.koala
        ),
        DataBook(
            "The Mountains Echoed", "khaled hosseini",
            "ffdvf", "long-", R.drawable.lighthouse
        ),
        DataBook(
            "White Tiger", "Arvind Adiga", "212",
            "65", R.drawable.penguins
        ),
        DataBook(
            "Mafia Queens", "S. Hussain Zaidi", "121",
            "212", R.drawable.tulips
        )

    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        recyclerdash = view.findViewById((R.id.recyclerdash))
        checkbtn = view.findViewById(R.id.btncheckinternet)

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
                dialog.setPositiveButton("Ok") { text, listener -> }
                dialog.setNegativeButton("Cancel") { text, listener -> }
                dialog.create()
                dialog.show()
            }
        }

        layoutlinear = LinearLayoutManager(activity)
        recycleadapter = DashboardRecyclerAdapter(activity as Context, bookInfoList)

        recyclerdash.layoutManager = layoutlinear
        recyclerdash.adapter = recycleadapter
        /*recyclerdash.addItemDecoration(
            DividerItemDecoration(
                recyclerdash.context,
                (layoutlinear as LinearLayoutManager).orientation
            )*/
        //)
        return view
    }
}


