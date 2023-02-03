package com.tarciodiniz.orgs.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import com.tarciodiniz.orgs.R
import com.tarciodiniz.orgs.database.AppDatabase
import com.tarciodiniz.orgs.databinding.ActivityListProductsBinding
import com.tarciodiniz.orgs.extensions.invokeActivity
import com.tarciodiniz.orgs.model.Product
import com.tarciodiniz.orgs.ui.recyclerView.adapter.ListProductAdapter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class ListProductsActivity : ActivityBaseUser() {

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
            refreshList()
        }
        lifecycleScope.launch {
            launch {
                user
                    .filterNotNull()
                    .collect {user ->
                        searchForUserProducts(user.id)
                    }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.filter_menu, menu)
        menuInflater.inflate(R.menu.menu_logout, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        configureLogoutButton(item)
        configureFilterButton(item)
        return super.onOptionsItemSelected(item)
    }

    fun refreshList() {
        binding.activityListSwipeRefresh.isRefreshing = true
        lifecycleScope.launch {
            productDao.getAll().collect { product ->
                adapter.update(product)
                binding.activityListSwipeRefresh.isRefreshing = false
            }
        }
    }

    private suspend fun searchForUserProducts(userID: String) {
        productDao.searchAllFromUser(userID).collect { product ->
            adapter.update(product)
        }
    }

    private fun configureLogoutButton(item: MenuItem) {
        when (item.itemId) {
            R.id.menu_logout -> {
                lifecycleScope.launch {
                    logoutUser()
                }
            }
        }
    }

    private fun configureFilterButton(item: MenuItem) {
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
                    productDao.getAll().first()
                else -> null
            }
            productsOrdered?.let {
                adapter.update(it)
            }
        }
    }

    private fun configureFab() {
        val fab = binding.activityListFab
        fab.setOnClickListener {
            invokeActivity(ProductFormActivity::class.java)
        }
    }

    private fun configureRecyclerView() {
        // working with recyclerView
        val recyclerView = binding.activityListRecyclerView
        recyclerView.adapter = adapter
    }
}