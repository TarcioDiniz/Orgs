package com.tarciodiniz.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.tarciodiniz.orgs.R
import com.tarciodiniz.orgs.database.AppDatabase
import com.tarciodiniz.orgs.databinding.ActivityListProductsBinding
import com.tarciodiniz.orgs.ui.recyclerView.adapter.ListProductAdapter


class ListProductsActivity : AppCompatActivity(R.layout.activity_list_products) {

    private val adapter = ListProductAdapter(
        context = this
    )

    private val binding by lazy {
        ActivityListProductsBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configureRecyclerView()
        configureFab()
        binding.activityListSwipeRefresh.setOnRefreshListener {
            onResume()
        }

    }

    override fun onResume() {
        super.onResume()
        val db = AppDatabase.getInstance(this)
        val productDao = db.productDao()
        adapter.update(productDao.getAll())
        binding.activityListSwipeRefresh.isRefreshing = false
    }

    private fun configureFab() {
        val fab = binding.activityListFab
        fab.setOnClickListener {
            goToProductForm()
        }
    }

    private fun goToProductForm() {
        val intent = Intent(this, ProductFormActivity::class.java)
        startActivity(intent)
    }

    private fun configureRecyclerView() {
        // working with recyclerView
        val recyclerView = binding.activityListRecyclerView
        recyclerView.adapter = adapter
    }

}