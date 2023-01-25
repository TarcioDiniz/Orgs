package com.tarciodiniz.orgs.ui.recyclerView.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tarciodiniz.orgs.databinding.ProductBinding
import com.tarciodiniz.orgs.extensions.tryToLoad
import com.tarciodiniz.orgs.model.Produto
import com.tarciodiniz.orgs.ui.activity.ProductView
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

class ListProductAdapter(
    private val context: Context, product: List<Produto>
) : RecyclerView.Adapter<ListProductAdapter.ViewHolder>() {

    private val dataProduct = product.toMutableList()

    class ViewHolder(private val binding: ProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var imageBitmap: String? = null

        init {
            itemView.setOnClickListener{
                val product = Produto(
                    name = binding.productName.text.toString(),
                    description = binding.productDescription.text.toString(),
                    value = removeCurrencyFormat(binding.productValue.text.toString()),
                    image = imageBitmap
                )

                val intent = Intent(itemView.context, ProductView::class.java)
                intent.putExtra("product", product as java.io.Serializable)
                itemView.context.startActivity(intent)
            }
        }

        fun bind(produto: Produto) {
            val name = binding.productName
            val description = binding.productDescription
            val value = binding.productValue

            name.text = produto.name
            description.text = produto.description
            val currencyValue: String = formatForCurrency(produto.value)
            value.text = currencyValue

            binding.imageView.visibility = if(produto.image != null){
                View.VISIBLE
            }else{
                View.GONE
            }

            binding.imageView.tryToLoad(produto.image)
            imageBitmap = produto.image

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
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val produto = dataProduct[position]
        holder.bind(produto)
    }

    override fun getItemCount(): Int = dataProduct.size
    @SuppressLint("NotifyDataSetChanged")
    fun update(products: List<Produto>) {
        this.dataProduct.clear()
        this.dataProduct.addAll(products)
        notifyDataSetChanged()
    }

}
