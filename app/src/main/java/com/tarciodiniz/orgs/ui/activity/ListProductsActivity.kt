package com.tarciodiniz.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tarciodiniz.orgs.R
import com.tarciodiniz.orgs.database.AppDatabase
import com.tarciodiniz.orgs.databinding.ActivityListProductsBinding
import com.tarciodiniz.orgs.model.Product
import com.tarciodiniz.orgs.ui.recyclerView.adapter.ListProductAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ListProductsActivity : AppCompatActivity(R.layout.activity_list_products) {

    private val adapter = ListProductAdapter(
        context = this
    )

    private val binding by lazy {
        ActivityListProductsBinding.inflate(layoutInflater)
    }

    private val productDao by lazy {
        AppDatabase.getInstance(this).productDao()
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

    fun refreshList() {
        onResume()
    }

    override fun onResume() {
        super.onResume()
        val scope = MainScope()
        binding.activityListSwipeRefresh.isRefreshing = true
        scope.launch {
            val product = withContext(Dispatchers.IO) {
                productDao.getAll()
            }
            adapter.update(product)
            binding.activityListSwipeRefresh.isRefreshing = false
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.filter_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        lifecycleScope.launch {
            val productsOrdered: List<Product>? = when (item.itemId) {
                R.id.filter_menu_asc_name ->
                    productDao.searchAllOrderByNameAsc()
                R.id.filter_menu_name_desc ->
                    productDao.searchAllOrderByNameDesc()
                R.id.filter_menu_asc_description ->
                    productDao.searchAllOrderByDescriptionAsc()
                R.id.filter_menu_description_desc ->
                    productDao.searchAllOrderByDescriptionDesc()
                R.id.filter_menu_asc_value ->
                    productDao.searchAllOrderByAscValue()
                R.id.filter_menu_discount_value ->
                    productDao.searchAllOrderByValueDesc()
                R.id.filter_menu_without_ordination ->
                    productDao.getAll()
                else -> null
            }
            productsOrdered?.let {
                adapter.update(it)
            }
        }
        return super.onOptionsItemSelected(item)
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