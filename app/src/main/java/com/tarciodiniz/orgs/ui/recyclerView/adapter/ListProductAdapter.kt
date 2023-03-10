package com.tarciodiniz.orgs.ui.recyclerView.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.tarciodiniz.orgs.R
import com.tarciodiniz.orgs.database.AppDatabase
import com.tarciodiniz.orgs.databinding.ProductBinding
import com.tarciodiniz.orgs.extensions.PRODUCT_KEY
import com.tarciodiniz.orgs.extensions.tryToLoad
import com.tarciodiniz.orgs.model.Product
import com.tarciodiniz.orgs.repository.ProductRepository
import com.tarciodiniz.orgs.ui.activity.ListProductsActivity
import com.tarciodiniz.orgs.ui.activity.ProductFormActivity
import com.tarciodiniz.orgs.ui.activity.ProductView
import com.tarciodiniz.orgs.webclient.product.ProductWebServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

class ListProductAdapter(
    private val context: Context, product: List<Product> = emptyList()
) : RecyclerView.Adapter<ListProductAdapter.ViewHolder>() {

    private val dataProduct = product.toMutableList()

    class ViewHolder(private val binding: ProductBinding, private val dataProduct: List<Product>) :
        RecyclerView.ViewHolder(binding.root) {

        private var imageBitmap: String? = null
        private val scope = CoroutineScope(Dispatchers.IO)


        init {
            itemView.setOnClickListener {

                val position = bindingAdapterPosition
                val product = dataProduct.toMutableList()[position].id

                val intent = Intent(itemView.context, ProductView::class.java)
                intent.putExtra(PRODUCT_KEY, product)
                itemView.context.startActivity(intent)
            }

            itemView.setOnLongClickListener {
                val popupMenu = PopupMenu(itemView.context, it)
                popupMenu.menuInflater.inflate(R.menu.popup_layout, popupMenu.menu)

                val position = bindingAdapterPosition
                val product = dataProduct.toMutableList()[position]

                val db = AppDatabase.getInstance(itemView.context)
                val productDao = db.productDao()
                val repository by lazy {
                    ProductRepository(productDao, ProductWebServices())
                }
                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.menu_edit -> {
                            Intent(itemView.context, ProductFormActivity::class.java).apply {
                                putExtra(PRODUCT_KEY, product.id)
                                itemView.context.startActivity(this)
                            }
                            true
                        }
                        R.id.menu_delete -> {
                            val userID = product.userID.toString()
                            scope.launch {
                                repository.delete(product)
                                withContext(Dispatchers.Main) {
                                    (itemView.context as ListProductsActivity).refreshList(userID)
                                }
                            }
                            true
                        }
                        else -> false
                    }
                }
                popupMenu.show()
                true
            }
        }


        fun bind(product: Product) {
            val name = binding.productName
            val description = binding.productDescription
            val value = binding.productValue

            name.text = product.name
            description.text = product.description
            val currencyValue: String = formatForCurrency(product.value)
            value.text = currencyValue

            binding.imageView.visibility = if (product.image != null) {
                View.VISIBLE
            } else {
                View.GONE
            }

            binding.imageView.tryToLoad(product.image)
            imageBitmap = product.image

        }

        private fun removeCurrencyFormat(value: String): BigDecimal {
            val formatter: NumberFormat = NumberFormat
                .getCurrencyInstance(Locale("pt", "br"))
            return BigDecimal(formatter.parse(value)!!.toString())
        }


        private fun formatForCurrency(value: BigDecimal): String {
            val formatter: NumberFormat = NumberFormat
                .getCurrencyInstance(Locale("pt", "br"))
            return formatter.format(value)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProductBinding.inflate(
            LayoutInflater.from(context), parent, false
        )
        return ViewHolder(binding, dataProduct)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = dataProduct[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = dataProduct.size

    @SuppressLint("NotifyDataSetChanged")
    fun update(products: List<Product>) {
        this.dataProduct.clear()
        this.dataProduct.addAll(products)
        notifyDataSetChanged()
    }

    private suspend fun Flow<List<Product>>.toProductList() = this.first()

    @SuppressLint("NotifyDataSetChanged")
    suspend fun updateFlow(products: Flow<List<Product>>) {
        products.toProductList().let { productsList ->
            this.dataProduct.clear()
            this.dataProduct.addAll(productsList)
            notifyDataSetChanged()
        }
    }


}
