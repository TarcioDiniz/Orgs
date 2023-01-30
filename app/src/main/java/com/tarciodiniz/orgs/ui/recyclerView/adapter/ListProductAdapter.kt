package com.tarciodiniz.orgs.ui.recyclerView.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.tarciodiniz.orgs.R
import com.tarciodiniz.orgs.database.AppDatabase
import com.tarciodiniz.orgs.databinding.ProductBinding
import com.tarciodiniz.orgs.extensions.tryToLoad
import com.tarciodiniz.orgs.model.Product
import com.tarciodiniz.orgs.ui.activity.ProductView
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

        init {
            itemView.setOnClickListener {

                val position = bindingAdapterPosition
                val product = dataProduct.toMutableList()[position]

                val intent = Intent(itemView.context, ProductView::class.java)
                intent.putExtra("product", product as java.io.Serializable)
                itemView.context.startActivity(intent)
            }

            itemView.setOnLongClickListener{
                val popupMenu = PopupMenu(itemView.context, it)
                popupMenu.menuInflater.inflate(R.menu.popup_layout, popupMenu.menu)
                val db = AppDatabase.getInstance(itemView.context)
                val productDao = db.productDao()
                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId){
                        R.id.menu_edit ->{
                            // Adicione o código desejado aqui para a ação 1
                            true
                        }
                        R.id.menu_delete -> {


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
        val produto = dataProduct[position]
        holder.bind(produto)
    }

    override fun getItemCount(): Int = dataProduct.size

    @SuppressLint("NotifyDataSetChanged")
    fun update(products: List<Product>) {
        this.dataProduct.clear()
        this.dataProduct.addAll(products)
        notifyDataSetChanged()
    }

}
