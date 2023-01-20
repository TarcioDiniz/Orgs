package com.tarciodiniz.orgs.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tarciodiniz.orgs.R
import com.tarciodiniz.orgs.dao.ProductsDao
import com.tarciodiniz.orgs.model.Produto
import com.tarciodiniz.orgs.ui.recyclerView.adapter.ListProductAdapter
import java.math.BigDecimal


class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // working with recyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val dao = ProductsDao()
        recyclerView.adapter = ListProductAdapter(
            context = this, product = dao.getProduct())

        val fab = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        fab.setOnClickListener{
            val intent = Intent(this, ProductFormActivity::class.java)
            startActivity(intent)
        }

    }
}