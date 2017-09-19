package com.github.colecionista.ui.main

import android.content.Intent
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.github.colecionista.R
import com.github.colecionista.ui.base.BaseActivity
import com.github.colecionista.data.model.Item
import com.github.colecionista.data.source.local.SharedPreferenceService
import com.github.colecionista.ui.additem.AddItemActivity
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.github.colecionista.ui.login.LoginActivity

open class MainActivity : BaseActivity<MainContract.View, MainContract.Presenter>(), MainContract.View, NavigationView.OnNavigationItemSelectedListener {

    override var mPresenter: MainContract.Presenter = MainPresenter()

    private var itemsRecycleView: RecyclerView? = null

     var mAdapter: ItemsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        setupNavigationDrawer(toolbar)
        setupItemsRecycleView()

        findViewById( R.id.fab_add_item_main_acitivity).setOnClickListener{fabButtonWasClicked()}
        mPresenter.loadItems()
    }

    private fun setupItemsRecycleView() {
        itemsRecycleView = findViewById(R.id.rv_main_activity) as RecyclerView
        itemsRecycleView?.setHasFixedSize(true)
        val mLayoutManager = LinearLayoutManager(this)
        itemsRecycleView?.setLayoutManager(mLayoutManager)
    }

    private fun setupNavigationDrawer(toolbar: Toolbar) {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)

        drawer.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                showProfile()
            }

            override fun onDrawerOpened(drawerView: View) {
            }

            override fun onDrawerClosed(drawerView: View) {
            }

            override fun onDrawerStateChanged(newState: Int) {

            }
        })

        toggle.syncState()
    }

    private fun showProfile() {
        var sharedPreferenceService = SharedPreferenceService(getContext())
        val userNameTextView = findViewById(R.id.tv_name_nav_header) as TextView?
        userNameTextView?.text = sharedPreferenceService.getUserName()
        val emailTextView = findViewById(R.id.tv_email_nav_header) as TextView?
        emailTextView?.text = sharedPreferenceService.getUserEmail()

        val imageView = findViewById(R.id.iv_profile_photo_nav_header) as ImageView?

        Glide.with(getContext()).load(sharedPreferenceService.getPhotoUri()).asBitmap().centerCrop().into(object : BitmapImageViewTarget(imageView) {
            override fun setResource(resource: Bitmap) {
                val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getContext().resources, resource)
                circularBitmapDrawable.isCircular = true
                imageView?.setImageDrawable(circularBitmapDrawable)
            }
        })

        val navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.bringToFront()
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun fabButtonWasClicked() {
        intent = Intent(baseContext, AddItemActivity::class.java)
        startActivity(intent)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d("MainPreseter", "onNavigationItemSelected:")

        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_logout -> {
               mPresenter.logout()
            }
        }

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun showItems(items: MutableList<Item>?) {
        mAdapter = ItemsAdapter(mPresenter, items!!)
        itemsRecycleView?.adapter = mAdapter
    }

    override fun showLoading() {
        findViewById(R.id.pb_downloading_items_activity_main).visibility = View.VISIBLE
    }

    override fun hideLoading() {
        findViewById(R.id.pb_downloading_items_activity_main).visibility = View.GONE
    }

    override fun startLoginActivity() {
        intent = Intent(baseContext, LoginActivity::class.java)
        startActivity(intent)
    }
}
