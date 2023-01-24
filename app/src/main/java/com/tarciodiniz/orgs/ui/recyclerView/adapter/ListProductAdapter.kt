package com.tarciodiniz.orgs.ui.recyclerView.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tarciodiniz.orgs.R
import com.tarciodiniz.orgs.databinding.ProductBinding
import com.tarciodiniz.orgs.model.Produto

class ListProductAdapter(
    private val context: Context, product: List<Produto>
) : RecyclerView.Adapter<ListProductAdapter.ViewHolder>() {

    private val dataProduct = product.toMutableList()

    class ViewHolder(binding: ProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(produto: Produto) {

            val name = itemView.findViewById<TextView>(R.id.product_name)
            val description = itemView.findViewById<TextView>(R.id.product_description)
            val value = itemView.findViewById<TextView>(R.id.product_value)

            name.text = produto.name
            description.text = produto.description
            value.text = produto.value.toPlainString()

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
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
    fun update(products: List<Produto>) {
        this.dataProduct.clear()
        this.dataProduct.addAll(products)
        notifyDataSetChanged()
    }

}
