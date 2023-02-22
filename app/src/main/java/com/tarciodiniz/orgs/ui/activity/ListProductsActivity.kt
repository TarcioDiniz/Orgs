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
import com.tarciodiniz.orgs.repository.ProductRepository
import com.tarciodiniz.orgs.ui.recyclerView.adapter.ListProductAdapter
import com.tarciodiniz.orgs.webclient.product.ProductWebServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
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

    private val productWebServices by lazy {
        ProductWebServices()
    }

    private val productRepository by lazy {
        ProductRepository(
            AppDatabase.getInstance(this).productDao(),
            ProductWebServices())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configureRecyclerView()
        configureFab()
        lifecycleScope.launch {
            launch {
                user
                    .filterNotNull()
                    .collect { user ->
                        searchForUserProducts(user.id)
                    }
            }
        }

         lifecycleScope.launch{
            user.filterNotNull().collect{user ->
                binding.activityListSwipeRefresh.setOnRefreshListener {
                    refreshList(user.id)
                }
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            productWebServices.getProducts()
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

    fun refreshList(userID: String) {
        lifecycleScope.launch {
            productRepository.searchAllFromUser(userID).collect { product ->
                adapter.update(product)
                binding.activityListSwipeRefresh.isRefreshing = false
            }
        }
    }

    private suspend fun searchForUserProducts(userID: String) {
        productRepository.searchAllFromUser(userID).collect { product ->
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
            user.filterNotNull().collect { user ->
                val userLogin = user.id
                val productsOrdered: Flow<List<Product>>? = when (item.itemId) {
                    R.id.filter_menu_asc_name ->
                        productDao.searchAllOrderByNameAsc(userLogin)
                    R.id.filter_menu_name_desc ->
                        productDao.searchAllOrderByNameDesc(userLogin)
                    R.id.filter_menu_asc_description ->
                        productDao.searchAllOrderByDescriptionAsc(userLogin)
                    R.id.filter_menu_description_desc ->
                        productDao.searchAllOrderByDescriptionDesc(userLogin)
                    R.id.filter_menu_asc_value ->
                        productDao.searchAllOrderByAscValue(userLogin)
                    R.id.filter_menu_discount_value ->
                        productDao.searchAllOrderByValueDesc(userLogin)
                    R.id.filter_menu_without_ordination ->
                        productDao.searchAllFromUser(userLogin)
                    else -> null
                }
                productsOrdered?.let {
                    adapter.updateFlow(it)
                }
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