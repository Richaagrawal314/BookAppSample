package com.internshala.bookhun.activity

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.internshala.bookhun.R
import com.internshala.bookhun.fragment.DashboardFragment
import com.internshala.bookhun.fragment.FavFragment
import com.internshala.bookhun.fragment.SearchFragment
import com.internshala.bookhun.fragment.aboutappfragment

class MainActivity : AppCompatActivity() {
    private lateinit var dr: DrawerLayout
    private lateinit var fl: FrameLayout
    private lateinit var cl: CoordinatorLayout
    private lateinit var nv: NavigationView

    private var prevmenuitem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dr = findViewById(R.id.drawerlayout)
        fl = findViewById(R.id.framelayout)
        cl = findViewById(R.id.coordinatorlayout)
        nv = findViewById(R.id.navigationview)
        setuptoolbar()

        openDashboard()

        val actionBartoggle = ActionBarDrawerToggle(
            this@MainActivity,
            dr,
            R.string.open_drawer,
            R.string.close_drawer
        )
        dr.addDrawerListener(actionBartoggle)
        actionBartoggle.syncState()

        nv.setNavigationItemSelectedListener {

            if (prevmenuitem != null) {
                prevmenuitem?.isChecked = false
            }
            it.isCheckable = true
            it.isChecked = true
            prevmenuitem = it

            when (it.itemId) {
                R.id.dashboard -> {
                    openDashboard()
                    dr.closeDrawers()
                }

                R.id.fovourite -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.framelayout,
                            FavFragment()
                        )
                        //  .addToBackStack("favourite").addToBackStack("favourite")
                        .commit()
                    supportActionBar?.title = "Favourite "

                    dr.closeDrawers()
                }

                R.id.myLibrary -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.framelayout,
                            SearchFragment()
                        )
                        //.addToBackStack("profile")
                        .commit()
                    supportActionBar?.title = "Search"

                    dr.closeDrawers()
                }

                R.id.aboutapp -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.framelayout,
                            aboutappfragment()
                        )
                        //.addToBackStack("AboutApp")
                        .commit()
                    supportActionBar?.title = "AboutApp "

                    dr.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            dr.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setuptoolbar() {

        supportActionBar?.title = "title bar"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onBackPressed() {
        when (supportFragmentManager.findFragmentById(R.id.framelayout)) {
            !is DashboardFragment -> openDashboard()
            else -> super.onBackPressed()
        }

    }

    private fun openDashboard() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.framelayout,
                DashboardFragment()
            )
            // .addToBackStack("Dashboard")
            .commit()
        supportActionBar?.title = "Dashboard "
        nv.setCheckedItem(R.id.dashboard)
    }

   /* override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)
        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu?.findItem(R.id.search_icon)

        //not yet functional
        val searchView = searchItem?.actionView as SearchView

        searchView.setSearchableInfo(manager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                searchView.setQuery("", false)
                searchItem.collapseActionView()
                Toast.makeText(this@MainActivity, "$query", Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Toast.makeText(this@MainActivity,"$newText",Toast.LENGTH_SHORT).show()
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }*/
}

