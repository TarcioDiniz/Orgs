package com.tarciodiniz.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tarciodiniz.orgs.R
import com.tarciodiniz.orgs.dao.ProductsDao
import com.tarciodiniz.orgs.ui.recyclerView.adapter.ListProductAdapter


class ListProductsActivity : AppCompatActivity(R.layout.activity_list_products) {

    private val dao = ProductsDao()

    private val adapter = ListProductAdapter(
        context = this, product = dao.getProduct()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureRecyclerView()
        configureFab()
    }

    override fun onResume() {
        super.onResume()
        adapter.update(dao.getProduct())
    }

    private fun configureFab() {
        val fab = findViewById<FloatingActionButton>(R.id.activity_list_fab)
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
        val recyclerView = findViewById<RecyclerView>(R.id.activity_list_recyclerView)
        recyclerView.adapter = adapter
    }

}