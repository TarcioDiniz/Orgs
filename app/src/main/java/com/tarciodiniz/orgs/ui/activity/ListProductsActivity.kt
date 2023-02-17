package com.tarciodiniz.orgs.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import com.tarciodiniz.orgs.R
import com.tarciodiniz.orgs.database.AppDatabase
import com.tarciodiniz.orgs.databinding.ActivityListProductsBinding
import com.tarciodiniz.orgs.extensions.invokeActivity
import com.tarciodiniz.orgs.model.Product
import com.tarciodiniz.orgs.ui.recyclerView.adapter.ListProductAdapter
import com.tarciodiniz.orgs.webclient.InitializerRetrofit
import com.tarciodiniz.orgs.webclient.services.model.json.ProductListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Call


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
            try {
                val response = InitializerRetrofit().productServices.getProducts()
                if (response.isSuccessful) {
                    response.body()?.let { products ->
                        Log.i("ProductAPI", "onCreate: $products")
                    }
                } else {
                    Log.e("ProductAPI", "onCreate: failed to get products")
                }
            } catch (e: Exception) {
                Log.e("ProductAPI", "onCreate: failed to get products", e)
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

    fun refreshList(userID: String) {
        lifecycleScope.launch {
            productDao.searchAllFromUser(userID).collect { product ->
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