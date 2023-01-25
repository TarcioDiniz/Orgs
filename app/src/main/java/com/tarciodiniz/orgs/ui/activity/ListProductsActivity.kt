package com.tarciodiniz.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tarciodiniz.orgs.R
import com.tarciodiniz.orgs.dao.ProductsDao
import com.tarciodiniz.orgs.databinding.ActivityListProductsBinding
import com.tarciodiniz.orgs.databinding.ProductBinding
import com.tarciodiniz.orgs.ui.recyclerView.adapter.ListProductAdapter


class ListProductsActivity : AppCompatActivity(R.layout.activity_list_products) {

    private val dao = ProductsDao()
    private val adapter = ListProductAdapter(
        context = this, product = dao.getProduct()
    )

    private val binding by lazy {
        ActivityListProductsBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configureRecyclerView()
        configureFab()
    }

    override fun onResume() {
        super.onResume()
        adapter.update(dao.getProduct())
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